-- Table: ENTRY
CREATE TABLE IF NOT EXISTS ENTRY (
    date    DATE    NOT NULL,
    user_id INTEGER REFERENCES USER (user_id)
                    NOT NULL,
    time    INTEGER,
    UNIQUE (
        date,
        user_id
    )
);

-- Table: GROUP
CREATE TABLE IF NOT EXISTS [GROUP] (
    ID INTEGER PRIMARY KEY
);

-- Table: IS_FRIENDS
CREATE TABLE [IS_FRIENDS ] (
    user_id_1 INTEGER REFERENCES USER (user_id),
    user_id_2 INTEGER REFERENCES USER (user_id)
);

-- Table: IS_MEMBER
CREATE TABLE IS_MEMBER (
    USER_ID  INTEGER REFERENCES USER (user_id),
    GROUP_ID INTEGER REFERENCES [GROUP] (ID)
);

-- Table: USER
CREATE TABLE USER (
    user_id INTEGER PRIMARY KEY,
    name    TEXT    NOT NULL,
    remind  BOOLEAN NOT NULL
                    DEFAULT (1),
    email   VARCHAR UNIQUE
);