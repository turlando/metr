SELECT DISTINCT
    route.code AS route_code,
    route.name AS route_name,
    route.type AS route_type,
    trip.destination AS trip_destination,
    stop_time.time AS stop_time_time
FROM route
JOIN trip ON trip.route_id = route.id
JOIN stop_time ON stop_time.trip_id = trip.id
JOIN stop ON stop.id = stop_time.stop_id
WHERE
    stop_time.time IS NOT NULL AND
    stop.code = ? AND
    stop_time.time > ? AND stop_time.time < ?
ORDER BY stop_time.time
