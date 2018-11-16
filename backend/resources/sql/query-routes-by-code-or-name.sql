  SELECT ALL
         id   AS route_id
       , code AS route_code
       , name AS route_name
       , type AS route_type
    FROM route
   WHERE
         code LIKE :?
      OR name LIKE :?
ORDER BY id ASC
