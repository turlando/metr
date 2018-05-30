(ns metr.core
  (:gen-class)
  (:require [clojure.java.jdbc :as jdbc]
            [metr.db :as db]
            [metr.gtfs :as gtfs]
            [mount.core :as mount]))

(defn insert-gtfs-data! [conn]
  (db/insert-stops! conn (gtfs/get-stops))
  (db/insert-routes! conn (gtfs/get-routes))
  (db/insert-shape-points! conn (gtfs/get-shape-points))
  (db/insert-trips! conn (gtfs/get-trips))
  (db/insert-stop-times! conn (gtfs/get-stop-times))
  nil)

(defn dump-db! []
  (jdbc/with-db-transaction [tx {:connection (db/get-connection
                                              "jdbc:sqlite:metr.db")}]
    (db/init-schema! tx)
    (insert-gtfs-data! tx))
  nil)

(defn start! []
  (mount/start)
  (jdbc/with-db-transaction [tx (db/db)]
    (db/init-schema! db/db)
    (insert-gtfs-data! db/db))
  nil)

(defn stop! []
  (mount/stop)
  nil)

(defn -main [& args]
  nil)
