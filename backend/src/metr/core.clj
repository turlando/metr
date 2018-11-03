(ns metr.core
  (:gen-class)
  (:require [clojure.java.jdbc :as jdbc]
            [metr.db :as db]
            [metr.gtfs :as gtfs]
            [metr.server :as server]))

(defn stop!
  [{:keys [db-conn http-server]
    :as   args}]
  (when (not= :none http-server)
    (server/stop! http-server))
  (db/close-connection! db-conn)
  nil)

(defn start!
  ([] (start! {}))
  ([{:keys [db-path start-http-server? import-data?]
     :or   {db-path            :memory
            start-http-server? true
            import-data?       true}
     :as   args}]
   (let [db-conn     (if (= :memory db-path)
                       (db/open-in-memory-connection!)
                       (db/open-file-connection! db-path))
         http-server (if start-http-server?
                       (server/start! {:db-conn db-conn})
                       :none)]
     (when import-data?
       (try
         (jdbc/with-db-transaction [tx db-conn]
           (db/init-schema! tx)
           (db/insert-stops! tx (gtfs/get-stops))
           (db/insert-routes! tx (gtfs/get-routes))
           (db/insert-shape-points! tx (gtfs/get-shape-points))
           (db/insert-trips! tx (gtfs/get-trips))
           (db/insert-stop-times! tx (gtfs/get-stop-times)))
         (catch Exception e
           (stop! {:db-conn     db-conn
                   :http-server http-server})
           (throw e))))
     {:db-conn     db-conn
      :http-server http-server})))

(defn -main [& args]
  nil)
