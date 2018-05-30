SELECT
    code,
    name,
    latitude,
    longitude
FROM stop
WHERE
    latitude > ? AND latitude < ? AND
    longitude > ? AND longitude < ?
ORDER BY code
