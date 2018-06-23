CREATE TABLE section (
	name TEXT PRIMARY KEY,
	pattern TEXT
);

CREATE TABLE sms (
    id TEXT PRIMARY KEY,
    sectionName TEXT,
    address TEXT,
    date NUMBER,
    body TEXT
)
