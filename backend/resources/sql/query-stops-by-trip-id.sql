SELECT
    stop.id AS stop_id,
    stop.code AS stop_code,
    stop.name AS stop_name,
    stop.latitude AS stop_latitude,
    stop.longitude AS stop_longitude
FROM stop_time, trip, stop
WHERE
    trip.id = ?
    AND trip.id = stop_time.trip_id
    AND stop_time.stop_id = stop.id
ORDER BY stop_time.sequence
