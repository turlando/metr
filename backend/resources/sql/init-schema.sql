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
CREATE INDEX
    stop_idx_latitude_longitude
    ON stop (latitude, longitude);
--;;

CREATE TABLE route (
    id INTEGER PRIMARY KEY,
    code TEXT,
    name TEXT,
    type INTEGER
);
--;;
CREATE INDEX route_idx_code ON route (code);
--;;
CREATE INDEX route_idx_name ON route (name);
--;;
CREATE INDEX route_idx_type ON route (type);
--;;

CREATE TABLE shape_point (
    shape_id INTEGER NOT NULL,
    sequence INTEGER NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    distance REAL NOT NULL,
    PRIMARY KEY (shape_id, sequence)
);
--;;
CREATE INDEX
    shape_point_idx_latitude_longitude
    ON shape_point (latitude, longitude);
--;;

CREATE TABLE trip (
    id TEXT PRIMARY KEY,
    service TEXT NOT NULL,
    destination TEXT NOT NULL,
    direction INTEGER NOT NULL,
    route_id INTEGER NOT NULL REFERENCES route (id),
    shape_id INTEGER NOT NULL REFERENCES shape_point (shape_id)
);
--;;
CREATE INDEX trip_idx_service ON trip (service);
--;;
CREATE INDEX trip_idx_route_id ON trip (route_id);
--;;
CREATE INDEX trip_idx_shape_id ON trip (shape_id);
--;;

CREATE TABLE stop_time (
    trip_id TEXT NOT NULL REFERENCES trip (id),
    stop_id INTEGER NOT NULL REFERENCES stop (id),
    sequence INTEGER NOT NULL,
    time INTEGER,
    distance REAL,
    PRIMARY KEY (trip_id, stop_id, sequence)
);
--;;
CREATE INDEX stop_time_idx_time ON stop_time (time);
