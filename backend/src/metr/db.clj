(ns metr.db
  (:require [clojure.string :as string]
            [clojure.java.jdbc :as jdbc]
            [mount.core :as mount]
            [metr.utils :as utils]))

(defn get-connection []
  (jdbc/get-connection
   {:connection-uri "jdbc:sqlite::memory:"}))

(mount/defstate ^{:on-reload :noop} db
  :start {:connection (get-connection)}
  :stop  (-> db :connection .close))

(defn init-schema! [db]
  (doall
   (map (fn [q] (jdbc/execute! db q))
        (-> (utils/slurp-resource "sql/init.sql")
            (string/split #"--;;"))))
  nil)

(defn insert-stops! [db stops]
  (jdbc/insert-multi! db "stop" stops)
  nil)

(defn insert-routes! [db routes]
  (jdbc/insert-multi! db "route" routes)
  nil)

(defn insert-trips! [db trips]
  (jdbc/insert-multi! db "trip" trips)
  nil)

(defn insert-timetables! [db timetables]
  (jdbc/insert-multi! db "timetable" timetables)
  nil)

(defn query-stops-in-rect [db
                           latitude_min latitude_max
                           longitude_min longitude_max]
  (jdbc/query
   db
   [(utils/slurp-resource "sql/query-stops-in-rect.sql")
    latitude_min latitude_max
    longitude_min longitude_max]))

(defn query-routes-by-stop-id [db stop-id]
  (jdbc/query
   db
   [(utils/slurp-resource "sql/query-routes-by-stop.sql")
    stop-id]))
