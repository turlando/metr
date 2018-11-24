  SELECT ALL
         id   AS route_id
       , code AS route_code
       , name AS route_name
       , type AS route_type
    FROM route
   WHERE
         code LIKE :query
      OR name LIKE :query
ORDER BY id ASC
   LIMIT :limit
