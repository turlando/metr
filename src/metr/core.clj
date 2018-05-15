(ns metr.core
  (:gen-class)
  (:require [mount.core :as mount]
            [metr.state :as state]
            [metr.db :as db]
            [metr.gtfs :as gtfs]))

(defn start! []
  (mount/start)
  (db/init-schema! state/db)
  (db/insert-stops! state/db (gtfs/get-stops))
  (db/insert-routes! state/db (gtfs/get-routes))
  (db/insert-trips! state/db (gtfs/get-trips))
  (db/insert-timetables! state/db (gtfs/get-timetables))
  nil)

(defn stop! []
  (mount/stop)
  nil)

(defn -main [& args]
  nil)
