-- May lord have mercy for my sinful soul
-- that produced such a query.
SELECT name
FROM route
WHERE id = (
    SELECT DISTINCT route_id
    FROM trip
    WHERE id IN (
        SELECT DISTINCT trip_id
        FROM timetable
        WHERE stop_id = ?))
