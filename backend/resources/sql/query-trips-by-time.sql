SELECT DISTINCT
    trip.id AS trip_id,
    trip.service AS trip_service,
    trip.destination AS trip_destination,
    trip.direction AS trip_direction
FROM stop_time, trip, route
WHERE
    stop_time.time > ?
    AND stop_time.time < ?
    AND stop_time.trip_id = trip.id
    AND trip.route_id = route.id
