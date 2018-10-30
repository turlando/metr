(ns metr.core
  (:gen-class)
  (:require [clojure.java.jdbc :as jdbc]
            [metr.db :as db]
            [metr.gtfs :as gtfs]
            [metr.server :as server]))

(defn stop!
  [{:keys [db-conn http-server]
    :as   args}]
  (when-not (= http-server :none)
    (server/stop! http-server))
  (-> db-conn :connection .close)
  nil)

(defn start!
  ([] (start! {}))
  ([{:keys [db-path start-http-server? import-data?]
     :or   {db-path            :memory
            start-http-server? true
            import-data?       true}
     :as   args}]
   (let [db-conn     (if (= db-path :memory)
                       (db/get-in-memory-connection)
                       (db/get-file-connection db-path))
         http-server (if start-http-server?
                       (server/start! {:db-conn db-conn})
                       :none)
         dataset     (gtfs/build-dataset)]
     (when import-data?
       (try
         (jdbc/with-db-transaction [tx db-conn]
           (db/init-schema! tx)
           (db/insert-stops! tx (-> dataset :stops))
           (db/insert-routes! tx (-> dataset :routes))
           (db/insert-shape-points! tx (-> dataset :shape-points))
           (db/insert-trips! tx (-> dataset :trips))
           (db/insert-stop-times! tx (-> dataset :stop-times)))
         (catch Exception e
           (stop! {:db-conn     db-conn
                   :http-server http-server})
           (throw e))))
     {:db-conn     db-conn
      :http-server http-server})))

(defn -main [& args]
  nil)
