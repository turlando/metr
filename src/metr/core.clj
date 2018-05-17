(ns metr.core
  (:gen-class)
  (:require [mount.core :as mount]
            [metr.db :as db]
            [metr.api :as api]
            [metr.gtfs :as gtfs]))

(defn start! []
  (mount/start)
  (db/init-schema! db/db)
  (db/insert-stops! db/db (gtfs/get-stops))
  (db/insert-routes! db/db (gtfs/get-routes))
  (db/insert-trips! db/db (gtfs/get-trips))
  (db/insert-timetables! db/db (gtfs/get-timetables))
  nil)

(defn stop! []
  (mount/stop)
  nil)

(defn -main [& args]
  nil)
