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

(defn query
  ([conn query]
   (jdbc/query conn [query]))
  ([conn query params]
   (jdbc/query
    conn
    (utils/sql-query-with-named-params->sql-query query params))))

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

(defn query-routes-by-code-or-name
  [conn {:keys [q limit]
         :or   {limit default-query-limit}
         :as   args}]
  (query
   conn
   (utils/slurp-resource "sql/query-routes-by-code-or-name.sql")
   {:query (str "%" q "%")
    :limit limit}))
