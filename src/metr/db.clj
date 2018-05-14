(ns metr.db
  (:require [clojure.string :as string]
            [clojure.java.jdbc :as jdbc]
            [metr.utils :as utils]))

(defn get-connection []
  (jdbc/get-connection
   {:connection-uri "jdbc:sqlite::memory:"}))

(defn init! [db]
  (doall
   (map (fn [q] (jdbc/execute! db q))
        (-> (utils/slurp-resource "sql/init.sql")
            (string/split #"--;;")))))

(defn insert-stops! [db stops]
  (jdbc/insert-multi! db "stop" stops))

(defn insert-routes! [db routes]
  (jdbc/insert-multi! db "route" routes))

(defn insert-trips! [db trips]
  (jdbc/insert-multi! db "trip" trips))

(defn insert-timetable! [db timetable]
  (jdbc/insert-multi! db "timetable" timetable))

(defn query-nearby-stops [db latitude longitude limit]
  (jdbc/query
   db
   [(utils/slurp-resource "sql/query-near-stops.sql")
    latitude longitude limit]))
