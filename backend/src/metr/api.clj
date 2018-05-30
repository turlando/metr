(ns metr.api
  (:require [metr.db :as db]
            [metr.utils :as utils]))

(defn get-stop-times-by-stop-code [code
                                   time-min time-max]
  (->> (db/query-stop-times-by-stop-code
        db/db
        code
        (utils/time->seconds time-min)
        (utils/time->seconds time-max))
       (map #(update % :timetable_time utils/seconds->time))))

(defn get-stops-in-rect
  [lat-min lat-max
   lon-min lon-max]
  (db/query-stops-in-rect db/db
                          lat-min lat-max
                          lon-min lon-max))
