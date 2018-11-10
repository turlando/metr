(ns metr.db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]
            [metr.utils :as utils]))

(def ^:private default-query-limit 250)

(defn open-file-connection! [file]
  {:connection (jdbc/get-connection
                {:connection-uri (str "jdbc:sqlite:" file)})})

(defn open-in-memory-connection! []
  {:connection (jdbc/get-connection
                {:connection-uri "jdbc:sqlite::memory:"})})

(defn close-connection! [conn]
  (.close ^org.sqlite.SQLiteConnection (:connection conn))
  nil)

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

(defn query-stops-by-coordinates
  [conn & {:keys [latitude-min longitude-min
                  latitude-max longitude-max
                  limit]
           :or   {limit default-query-limit}
           :as   args}]
  {:pre [(every? (partial contains? args)
                 [:latitude-min :longitude-min
                  :latitude-max :longitude-max])]}
  (utils/sql-query
   conn
   (utils/slurp-resource "sql/query-stops-by-coordinates.sql")
   {:latitude_min  latitude-min
    :latitude_max  latitude-max
    :longitude_min longitude-min
    :longitude_max longitude-max
    :limit         limit}))

(defn query-stop-times-by-stop-code [conn code time-min time-max]
  (jdbc/query
   conn
   [(utils/slurp-resource "sql/query-stop-times-by-stop-code.sql")
    code time-min time-max]))

(defn query-stops-by-trip-id [conn trip-id]
  (jdbc/query
   conn
   [(utils/slurp-resource "sql/query-stops-by-trip-id.sql")
    trip-id]))
