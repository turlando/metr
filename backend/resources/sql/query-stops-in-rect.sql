SELECT *
FROM stop
WHERE
latitude > ? AND latitude < ? AND
longitude > ? AND longitude < ?
