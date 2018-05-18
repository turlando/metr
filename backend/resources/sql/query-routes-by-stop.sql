SELECT DISTINCT route.code, route.name, route.type, trip.destination
FROM route
JOIN trip ON trip.route_id = route.id
JOIN timetable ON timetable.trip_id = trip.id
JOIN stop ON stop.id = timetable.stop_id
WHERE stop.id = ?
