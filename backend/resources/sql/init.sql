CREATE TABLE stop (
    id INTEGER PRIMARY KEY,
    code INTEGER NOT NULL,
    name TEXT NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL
);
--;;
CREATE INDEX stop_idx_code ON stop (code);
--;;
CREATE INDEX stop_idx_name ON stop (name);
--;;
CREATE INDEX stop_idx_latitude_longitude ON stop (latitude, longitude);
--;;

CREATE TABLE route (
    id INTEGER PRIMARY KEY,
    code TEXT,
    name TEXT,
    type INTEGER
);
--;;
CREATE INDEX route_idx_name_short ON route (code);
--;;
CREATE INDEX route_idx_name_long ON route (name);
--;;
CREATE INDEX route_idx_name_type ON route (type);
--;;

CREATE TABLE trip (
    id TEXT PRIMARY KEY,
    service TEXT NOT NULL,
    route_id INTEGER NOT NULL REFERENCES route (id),
    destination TEXT NOT NULL,
    direction INTEGER NOT NULL
);
--;;
CREATE INDEX trip_idx_service ON trip (service);
--;;

CREATE TABLE timetable (
    trip_id TEXT NOT NULL REFERENCES trip (id),
    stop_id INTEGER NOT NULL REFERENCES stop (id),
    sequence INTEGER NOT NULL,
    time INTEGER NOT NULL
);
