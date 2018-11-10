  SELECT ALL
         code      AS stop_code
       , name      AS stop_name
       , latitude  AS stop_latitude
       , longitude AS stop_longitude
    FROM stop
   WHERE
         latitude > :latitude_min
     AND latitude < :latitude_max
     AND longitude > :longitude_min
     AND longitude < :longitude_max
   LIMIT :limit
