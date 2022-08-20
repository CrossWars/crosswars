import asyncio
import functools
import logging
import datetime
import random
import statistics
from typing import Optional, Any, Mapping

from pytz import timezone
import pytz

import logging
from telegram import Update, InlineQueryResultArticle, InputTextMessageContent, constants
from telegram.ext import Updater, ApplicationBuilder, ContextTypes, CommandHandler, InlineQueryHandler, MessageHandler, \
    filters, Application

# Used for custom webhooka
import uvicorn
from starlette.applications import Starlette
from starlette.requests import Request
from starlette.responses import PlainTextResponse, Response
from starlette.routing import Route

import config
import crosswars
from utilities import Leaderboard, UserId, Time

logging.basicConfig(
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    level=logging.INFO
)

"""
Chat data layout

context.chat_data = {
    name_map: {
        id0: name
    },
    daily_times: {
        id0: time
    },
    leaderboard_msg: msgId
}
"""

"""def validate_user(func):
    @functools.wraps(func)
    def wrapper(update: Update, context: ContextTypes.DEFAULT_TYPE):
        return func(update, context)
"""


async def validate_user(update, context) -> Optional[Any]:
    r = await crosswars.get_telegram_user(update.message.from_user.id)
    if r.status_code == 204:
        await context.bot.send_message(chat_id=update.effective_chat.id,
                                       text='It looks like your Telegram account is not linked to a CrossWars user. '
                                            'Please link your account at crosswars.xyz on your user page.')
        return None
    if r.status_code != 200:
        await context.bot.send_message(chat_id=update.effective_chat.id, text='Something went wrong, please try again')
        return None
    return r.json()


# TODO: Rewrite for version 20
async def migrate(update: Update, context: ContextTypes.DEFAULT_TYPE):
    chat_id = update.message.chat_id
    if chat_id != config.group_id or update.message.from_user.id != config.admin_id:
        return
    # Create telegram group on backend
    response = await crosswars.create_group(group_id=chat_id, name=update.message.chat.title)
    if response.status_code != 200:
        context.bot.send_message(chat_id, 'Failed to create CrossWars group, please try again')
        return
    context.bot.send_message(chat_id, 'Created CrossWars group')

    # add each member to group TODO: NEED ENDPOINT + FUNCTION FOR ADDING TELEGRAM USERS TO GROUP
    for telegram_id in context.chat_data['ids']:
        response = await crosswars.add_to_group(telegram_id=telegram_id, group_id=chat_id)
        if response.status_code != 200:
            # TODO: CHECK RESPONSE IF USER IS ALREADY IN GROUP, IF USER DOESNT EXIST, ETC.
            context.bot.send_message(chat_id, 'Failed to add all users to CrossWars group, please try again')
            return
    context.bot.send_message(chat_id, 'Added all users to CrossWars group')

    # record all entries in group
    for user_name in context.chat_data['overall']:
        telegram_id = context.chat_data['id_mappings'][user_name]
        times = context.chat_data['overall'][user_name]
        response = await crosswars.add_times_bulk(telegram_id=telegram_id, times=times,
                                                  dates=context.chat_data['overallDates'])
        if response.status_code != 200:
            context.bot.send_message(chat_id, f'Failed to record times for {user_name} in CrossWars, please try again')
            return
    context.bot.send_message(chat_id, 'Recorded all user times in CrossWars')

    # sync all wins in group
    response = await crosswars.sync_wins(chat_id)
    if response.status_code != 200:
        context.bot.send_message(chat_id, 'Failed to record wins in CrossWars, please try again')
        return
    context.bot.send_message(chat_id, 'Recorded all wins in CrossWars')
    context.bot.send_message(chat_id, 'Migration complete 😄')


async def start(update: Update, context: ContextTypes.DEFAULT_TYPE):
    # TODO: ADD MORE TO WELCOME MESSAGE
    await context.bot.send_message(chat_id=update.effective_chat.id, text='Welcome to the CrossWars telegram bot!')
    if update.effective_chat.type in ('group', 'supergroup'):
        # check if group exists, prompt to create with /start_group
        response = await crosswars.get_group(update.effective_chat.id)
        if response.status_code == 204:
            # MESSAGE TO CREATE GROUP WITH /start_group
            await context.bot.send_message(chat_id=update.effective_chat.id,
                                           text='Start a daily crossword leaderboard in this group using /start_group')
            return
        if response.status_code != 200:
            await context.bot.send_message(chat_id=update.effective_chat.id,
                                           text='Something went wrong, please try again')
            return
    elif update.effective_chat.type == 'private':
        # Get user, sending message if not
        user = await validate_user(update, context)
        if not user:
            return
        if not user['remind']:
            await context.bot.send_message(chat_id=update.effective_chat.id,
                                           text=f'Looks like you aren\'t using reminders. Try them out with '
                                                f'/start_reminders')


async def start_group(update: Update, context: ContextTypes.DEFAULT_TYPE):
    user = await validate_user(update, context)
    if not user:
        return
    if update.effective_chat.type in ('group', 'supergroup'):
        response = await crosswars.create_group(group_id=update.effective_chat.id,
                                                name=update.effective_chat.title,
                                                created_by=update.message.from_user.id)
        if response.status_code not in (200, 204):
            await context.bot.send_message(chat_id=update.effective_chat.id,
                                           text='Something went wrong, please try again')
            return
        group = response.json()
        await crosswars.add_to_group(telegram_id=update.message.from_user.id,
                                     group_id=group['group_id'])


async def add_time(update: Update, context: ContextTypes.DEFAULT_TYPE):
    # add time for user
    time_strs = (update.message.text.partition(':'))
    time = 60 * int(time_strs[0] if len(time_strs[0]) != 0 else "0") + int(time_strs[2])
    response = await crosswars.add_time(telegram_id=update.message.from_user.id, time=time)
    if response.status_code == 204:
        await update.effective_chat.send_message(
            text='It looks like your Telegram account is not linked to a CrossWars user. Please link your account at '
                 'crosswars.xyz on your user page.')
        return
    if response.status_code != 200:
        await update.effective_chat.send_message(text='An error occurred, please try again')
        return

    if update.effective_chat.type in (constants.ChatType.GROUP, constants.ChatType.SUPERGROUP):
        # TODO: Test in group
        response = await crosswars.add_to_group(telegram_id=update.message.from_user.id,
                                                group_id=update.effective_chat.id)
        if response.status_code not in (201, 303):
            # TODO: Add invite link here
            await update.effective_chat.send_message(text='An error occurred adding you to the group, please send '
                                                          'time again or join group via website invite link')
            return

        # Add to leaderboard, cache time
        if 'name_map' not in context.chat_data:
            context.chat_data['name_map']: Mapping[UserId, str] = dict()
        context.chat_data['name_map'][update.message.from_user.id] = update.message.from_user.first_name

        if 'daily_times' not in context.chat_data:
            context.chat_data['daily_times']: Mapping[UserId, Time] = dict()
        context.chat_data['daily_times'][update.message.from_user.id] = time

        # Create leaderboard
        leaderboard = Leaderboard(context.chat_data['daily_times'], context.chat_data['name_map'])
        if 'leaderboard_msg' in context.chat_data:
            context.bot.edit_message_text(text=str(leaderboard),
                                          chat_id=update.effective_chat.id,
                                          message_id=context.chat_data['leaderboard_msg'])
        else:
            msg = await update.effective_chat.send_message(text=str(leaderboard))
            context.chat_data['leaderboard_msg'] = msg.id
            await update.effective_chat.pin_message(msg.id)
    elif update.effective_chat.type == constants.ChatType.PRIVATE:
        await context.bot.send_message(chat_id=update.effective_chat.id,
                                       text=f'Time added')


async def unknown(update: Update, context: ContextTypes.DEFAULT_TYPE):
    await context.bot.send_message(chat_id=update.effective_chat.id, text="Sorry, I didn't understand that command.")


"""def main():
    group = 8746877557406
    # group = crossworddata.get_group(group)
    user = crossword_data.get_user(104600847390556727159)
    botPersistence = PicklePersistence(filename='bot-data')
    updater = Updater(config.api_key, persistence=botPersistence, use_context=True)

    # Get the dispatcher to register handlers
    dp = updater.dispatcher
    print(user)
"""

async def main() -> None:
    # Set updater to None to use custom webhook server
    application = Application.builder().token(config.api_key).updater(None).build()

    start_handler = CommandHandler('start', start)
    application.add_handler(start_handler)

    application.add_handler(MessageHandler(filters.Regex(r'^[\d]*:[\d]{2}$'), add_time))

    # Other handlers
    unknown_handler = MessageHandler(filters.COMMAND, unknown)
    application.add_handler(unknown_handler)

    # TODO SET TO IP
    await application.bot.set_webhook(url=f'{config.bot_url}/telegram',
                                      certificate=config.public_key,
                                      secret_token=config.secret_token,
                                      )

    async def telegram(request: Request) -> Response:
        """Handle incoming Telegram updates by putting them into the `update_queue`"""
        await application.update_queue.put(
            Update.de_json(data=await request.json(), bot=application.bot)
        )
        return Response()

    application.run_polling()
    starlette_app = Starlette(
        routes=[
            Route("/telegram", telegram, methods=["POST"]),
        ]
    )
    webserver = uvicorn.Server(
        config=uvicorn.Config(
            app=starlette_app,
            port=config.bot_port,
            use_colors=False,
            host="127.0.0.1",

        )

    )
    async with application:
        await application.start()
        await webserver.serve()
        await application.stop()

if __name__ == '__main__':
    asyncio.run(main())