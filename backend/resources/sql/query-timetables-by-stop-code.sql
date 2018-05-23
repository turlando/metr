SELECT DISTINCT
    route.code AS route_code,
    route.name AS route_name,
    route.type AS route_type,
    trip.destination AS trip_destination,
    timetable.time AS timetable_time
FROM route
JOIN trip ON trip.route_id = route.id
JOIN timetable ON timetable.trip_id = trip.id
JOIN stop ON stop.id = timetable.stop_id
WHERE
    stop.code = ? AND
    timetable.time > ? AND timetable.time < ?
ORDER BY timetable.time
