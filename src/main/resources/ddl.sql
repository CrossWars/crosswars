-- Table: ENTRY
CREATE TABLE IF NOT EXISTS ENTRY (
    date    DATE    NOT NULL,
    time    INTEGER,
    user_id TEXT REFERENCES USER (user_id) NOT NULL,
    UNIQUE (
        date,
        user_id
    )
);

-- Table: GROUP
CREATE TABLE IF NOT EXISTS [GROUP] (
    ID TEXT PRIMARY KEY
);

-- Table: IS_FRIENDS
CREATE TABLE [IS_FRIENDS ] (
    user_id_1 TEXT REFERENCES USER (user_id),
    user_id_2 TEXT REFERENCES USER (user_id)
);

-- Table: IS_MEMBER
CREATE TABLE IS_MEMBER (
    USER_ID  TEXT REFERENCES USER (user_id),
    GROUP_ID TEXT REFERENCES [GROUP] (ID)
);

-- Table: USER
CREATE TABLE USER (
    user_id TEXT PRIMARY KEY,
    name    TEXT    NOT NULL,
    remind  BOOLEAN NOT NULL DEFAULT (1),
    email   VARCHAR UNIQUE
);