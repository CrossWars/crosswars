create table if not exists player(
   email text not null,
   name text not null,
   telegram_id text,
   primary key(email)
);

create table if not exists friends(
    friend_1_email int not null,
    friend_2_email int not null,
    primary key(friend_1_email, friend_2_email)
);

create table if not exists puzzle_entry(
    player_email text not null,
    entry_date date not null,
    puzzle_time int not null,
    primary key (player_email)
);