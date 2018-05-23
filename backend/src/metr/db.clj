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
        (-> (utils/slurp-resource "sql/init-schema.sql")
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

(defn query-timetable-by-stop-code [db code time-min time-max]
  (jdbc/query
   db
   [(utils/slurp-resource "sql/query-timetables-by-stop-code.sql")
    code time-min time-max]))

(defn query-stops-in-rect [db
                           lat-min lat-max
                           lon-min lon-max]
  (jdbc/query
   db
   [(utils/slurp-resource "sql/query-stops-in-rect.sql")
    lat-min lat-max
    lon-min lon-max]))
