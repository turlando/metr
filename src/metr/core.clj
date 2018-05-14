(ns metr.core
  (:gen-class)
  (:require [mount.core :as mount]
            [metr.state :as state]
            [metr.db :as db]
            [metr.gtfs :as gtfs]))

(defn -main
  [& args]
  (mount/start)
  (db/init! state/db)
  (db/insert-stops! state/db (gtfs/get-stops))
  (db/insert-routes! state/db (gtfs/get-routes))
  (db/insert-trips! state/db (gtfs/get-trips))
  (db/insert-timetables! state/db (gtfs/get-timetables))
  nil)
