(ns metr.db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]
            [metr.utils :as utils]))

(defn open-file-connection! [file]
  {:connection (jdbc/get-connection
                {:connection-uri (str "jdbc:sqlite:" file)})})

(defn open-in-memory-connection! []
  {:connection (jdbc/get-connection
                {:connection-uri "jdbc:sqlite::memory:"})})

(defn close-connection! [conn]
  (.close ^org.sqlite.SQLiteConnection (:connection conn))
  nil)

(defn query [conn params]
  (jdbc/query conn params
              {:as-arrays? true}))

(defn init-schema! [conn]
  (let [statements (-> (utils/slurp-resource "sql/init-schema.sql")
                       (string/split #"--;;"))]
    (doseq [statement statements]
      (jdbc/execute! conn statement)))
  nil)

(defn insert-stops! [conn stops]
  (jdbc/insert-multi! conn "stop" stops)
  nil)

(defn insert-routes! [conn routes]
  (jdbc/insert-multi! conn "route" routes)
  nil)

(defn insert-shape-points! [conn shape-points]
  (jdbc/insert-multi! conn "shape_point" shape-points)
  nil)

(defn insert-trips! [conn trips]
  (jdbc/insert-multi! conn "trip" trips)
  nil)

(defn insert-stop-times! [conn stop-times]
  (jdbc/insert-multi! conn "stop_time" stop-times)
  nil)

(defn query-stops-in-rect [conn
                           lat-min lat-max
                           lon-min lon-max]
  (query
   conn
   [(utils/slurp-resource "sql/query-stops-in-rect.sql")
    lat-min lat-max
    lon-min lon-max]))

(defn query-stop-times-by-stop-code [conn code time-min time-max]
  (query
   conn
   [(utils/slurp-resource "sql/query-stop-times-by-stop-code.sql")
    code time-min time-max]))

(defn query-stops-by-trip-id [conn trip-id]
  (query
   conn
   [(utils/slurp-resource "sql/query-stops-by-trip-id.sql")
    trip-id]))
