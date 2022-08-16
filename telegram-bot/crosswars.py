import httpx
from httpx import Response

import config

dev = True
auth_header = {'x-internal-access-token': config.crosswars_token}


def build_url(endpoint: str) -> str:
    base = 'https://crosswars:8080/crosswars/api'
    if config.dev:
        base = 'http://localhost:8080/crosswars/api'
    return base + endpoint


async def get_group(group_id) -> Response:
    payload = {'group_id': group_id}
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.get(build_url('/groups/ids'), params=payload)
    return response


async def add_to_group(telegram_id, group_id) -> Response:
    payload = {'group_id': group_id, 'telegram_id': telegram_id}
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.post(build_url('/groups/users/telegram'), params=payload, headers=auth_header)
    return response


async def create_group(group_id, name, created_by) -> Response:
    payload = {'group_id': group_id, 'name': name, 'created_by': created_by}
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.post(build_url('/groups/telegram'), json=payload, headers=auth_header)
    return response


async def get_user(user_id) -> Response:
    payload = {'user_id': user_id}
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.get(build_url('/users/ids'), params=payload, headers=auth_header)
    return response


async def get_telegram_user(telegram_id) -> Response:
    payload = {'telegram_id': telegram_id}
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.get(build_url('/users/ids/telegram'), params=payload, headers=auth_header)
    return response


async def add_time(telegram_id, time) -> Response:
    payload = {'telegram_id': telegram_id, 'time': time}
    # POST requests require user_id as parameter TODO: CHECK THAT THIS WORKS WITHOUT UID PARAM, MAKE TELEGRAM ADD TIME ENDPOINT
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.post(build_url('/entries/telegram'), json=payload, headers=auth_header)
    return response


async def add_times_bulk(telegram_id, times, dates) -> Response:
    assert len(times) == len(dates)
    payload = []
    for i in range(len(times)):
        if not times[i]:
            continue
        payload.append(
            {'telegram_id': telegram_id,
             'date': dates[i],
             'time': times[i]
             })
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.post(build_url('/entries/bulk/telegram'), json=payload, headers=auth_header)
    return response


async def get_times(telegram_id) -> Response:
    payload = {'telegram_id': telegram_id}
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.get(build_url('/entries/users/telegram'), params=payload, headers=auth_header)
    return response


async def sync_wins(group_id) -> Response:
    payload = {'group_id': group_id}
    async with httpx.AsyncClient(verify=False) as client:
        response = await client.put(build_url('/wins/sync'), params=payload, headers=auth_header)
    return response
