(ns metr.api
  (:require [metr.db :as db]
            [metr.utils :as utils]))

(defn get-stop-times-by-stop-code
  [db-conn
   code time-min time-max]
  (->> (db/query-stop-times-by-stop-code
        db-conn
        code
        (utils/time->seconds time-min)
        (utils/time->seconds time-max))
       (map #(update % :stop_time_time utils/seconds->time))))

(defn get-stops-in-rect
  [db-conn
   latitude-min  latitude-max
   longitude-min longitude-max]
  (db/query-stops-in-rect
   db-conn
   :latitude-min  latitude-min
   :latitude-max  latitude-max
   :longitude-min longitude-min
   :longitude-max longitude-max))
