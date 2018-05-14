SELECT *
FROM stop
ORDER BY (ABS(latitude - ?) + ABS(longitude - ?)) ASC
LIMIT ?
