SELECT DISTINCT
    stop.code AS stop_code,
    stop.name AS stop_name,
    stop.latitude AS stop_latitude,
    stop.longitude AS stop_longitude,
    route.code AS route_code,
    route.name AS route_name,
    route.type AS route_type,
    timetable.time AS timetable_time
FROM route
JOIN trip ON trip.route_id = route.id
JOIN timetable ON timetable.trip_id = trip.id
JOIN stop ON stop.id = timetable.stop_id
WHERE
    route.code = ?
ORDER BY timetable.time
