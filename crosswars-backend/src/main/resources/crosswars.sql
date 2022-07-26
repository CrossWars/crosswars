-- Table: USER
CREATE TABLE IF NOT EXISTS USER
(
    user_id TEXT PRIMARY KEY,
    telegram_id TEXT UNIQUE,
    name    TEXT    NOT NULL,
    remind  BOOLEAN NOT NULL DEFAULT (1),
    email   VARCHAR UNIQUE
);

-- Table: ENTRY
CREATE TABLE IF NOT EXISTS ENTRY
(
    date    DATE                           NOT NULL,
    time    INTEGER,
    user_id TEXT REFERENCES USER (user_id) NOT NULL,
    UNIQUE (
            date,
            user_id
        )
);

-- Table: CROSSGROUP
CREATE TABLE IF NOT EXISTS CROSSGROUP
(
    group_id   TEXT PRIMARY KEY,
    name       TEXT UNIQUE,
    created_by TEXT REFERENCES USER (user_id)
);

-- Table: IS_FRIENDS
CREATE TABLE IF NOT EXISTS IS_FRIENDS
(
    user_id_1 TEXT REFERENCES USER (user_id),
    user_id_2 TEXT REFERENCES USER (user_id)
);

-- Table: IS_MEMBER
CREATE TABLE IF NOT EXISTS IS_MEMBER
(
    user_id  TEXT REFERENCES USER (user_id),
    group_id TEXT REFERENCES CROSSGROUP (group_id)
);



-- Table: WINS
CREATE TABLE IF NOT EXISTS WINS
(
    user_id  TEXT REFERENCES USER (user_id),
    group_id TEXT REFERENCES CROSSGROUP (group_id),
    date     DATE NOT NULL,
    PRIMARY KEY (
                 user_id,
                 group_id,
                 date
        )
);