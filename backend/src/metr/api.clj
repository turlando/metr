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
