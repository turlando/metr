(ns metr.api
  (:require [metr.db :as db]
            [metr.utils :as utils]))

(defn get-timetable-by-stop-code [code]
  (->> (db/query-timetable-by-stop-code db/db code)
       (map #(update % :timetable_time utils/seconds->time))))

(defn get-stops-in-rect [lat-min lat-max
                         lon-min lon-max]
  (db/query-stops-in-rect db/db
                          lat-min lat-max
                          lon-min lon-max))

(defn get-timetable-in-rect-in-time-by-stop-code
  [lat-min lat-max
   lon-min lon-max
   time-min time-max]
  (->> (db/query-timetable-in-rect-in-time db/db
                                           lat-min lat-max
                                           lon-min lon-max
                                           (utils/time->seconds time-min)
                                           (utils/time->seconds time-max))
       (map #(update % :timetable_time utils/seconds->time))
       (group-by :stop_code)))

(defn get-stops-by-route-code [code]
  (->> (db/query-stops-by-route-code db/db code)
       (map #(update % :timetable_time utils/seconds->time))))
