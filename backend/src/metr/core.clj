(ns metr.core
  (:gen-class)
  (:require [clojure.java.jdbc :as jdbc]
            [metr.db :as db]
            [metr.gtfs :as gtfs]
            [metr.server :as server]))

(defn- insert-gtfs-data! [db-conn]
  (let [data (gtfs/build-dataset)]
    (db/insert-stops! db-conn (-> data :stops))
    (db/insert-routes! db-conn (-> data :routes))
    (db/insert-shape-points! db-conn (-> data :shape-points))
    (db/insert-trips! db-conn (-> data :trips))
    (db/insert-stop-times! db-conn (-> data :stop-times)))
  nil)

(defn stop!
  [{:keys [db-conn http-server]
    :as   args}]
  (server/stop! http-server)
  (-> db-conn :connection .close)
  nil)

(defn start!
  ([] (start! {}))
  ([{:keys [db-path]
     :or   {db-path :memory}
     :as   args}]
   (let [db-conn     (if (= db-path :memory)
                       (db/get-in-memory-connection)
                       (db/get-file-connection db-path))
         http-server (server/start! {:db-conn db-conn})]
     (try
       (jdbc/with-db-transaction [tx db-conn]
         (db/init-schema! tx)
         (insert-gtfs-data! tx))
       (catch Exception e
         (stop! {:db-conn     db-conn
                 :http-server http-server})
         (throw e)))
     {:db-conn     db-conn
      :http-server http-server})))

(defn -main [& args]
  nil)
