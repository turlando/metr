(ns metr.core
  (:gen-class)
  (:require [clojure.java.jdbc :as jdbc]
            [metr.db :as db]
            [metr.gtfs :as gtfs]
            [metr.server :as server]
            [mount.core :as mount]))

(defn insert-gtfs-data! [db]
  (let [data (gtfs/build-dataset)]
    (db/insert-stops! db (-> data :stops))
    (db/insert-routes! db (-> data :routes))
    (db/insert-shape-points! db (-> data :shape-points))
    (db/insert-trips! db (-> data :trips))
    (db/insert-stop-times! db (-> data :stop-times)))
  nil)

(defn dump-db! []
  (jdbc/with-db-transaction [tx {:connection (db/get-connection
                                              "jdbc:sqlite:metr.db")}]
    (db/init-schema! tx)
    (insert-gtfs-data! tx))
  nil)

(defn stop! []
  (mount/stop)
  nil)

(defn start! []
  (db/register-mount!)
  (server/register-mount!)
  (mount/start)

  (try
    (jdbc/with-db-transaction [tx db/db]
      (db/init-schema! db/db)
      (insert-gtfs-data! db/db))
    (catch Exception e
      (stop!)
      (throw e)))

  nil)

(defn -main [& args]
  nil)
