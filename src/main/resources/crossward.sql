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

-- Table: CROSSGROUP
CREATE TABLE IF NOT EXISTS CROSSGROUP (
    group_id TEXT PRIMARY KEY,
    name TEXT
);

-- Table: IS_FRIENDS
CREATE TABLE [IS_FRIENDS ] (
    user_id_1 TEXT REFERENCES USER (user_id),
    user_id_2 TEXT REFERENCES USER (user_id)
);

-- Table: IS_MEMBER
CREATE TABLE IS_MEMBER (
    user_id  TEXT REFERENCES USER (user_id),
    group_id TEXT REFERENCES CROSSGROUP (group_id)
);

-- Table: USER
CREATE TABLE USER (
    user_id TEXT PRIMARY KEY,
    name    TEXT    NOT NULL,
    remind  BOOLEAN NOT NULL DEFAULT (1),
    email   VARCHAR UNIQUE
);

-- Table: WINS
CREATE TABLE WINS (
    user_id  TEXT    REFERENCES USER (user_id),
    group_id INTEGER REFERENCES CROSSGROUP (group_id),
    wins     INTEGER NOT NULL,
    PRIMARY KEY (
        user_id,
        group_id
    )
);