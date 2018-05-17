(ns metr.api
  (:require [clojure.data.json :as json]
            [mount.core :as mount]
            [org.httpkit.server :as server]
            [ring.middleware.params :as middleware.params]
            [compojure.core :as compojure]
            [metr.db :as db]))

(defn- get-stops-in-rect-handler [request]
  (let [lat-min (-> request :params (get "lat-min"))
        lat-max (-> request :params (get "lat-max"))
        lon-min (-> request :params (get "lon-min"))
        lon-max (-> request :params (get "lon-max"))]
    {:status  200
     :headers {"Content-Type" "application/json; charset=utf-8"
               "Access-Control-Allow-Origin" "http://localhost:8081"}
     :body    (-> (db/query-stops-in-rect
                   db/db
                   lat-min lat-max
                   lon-min lon-max)
                  json/write-str)}))

(compojure/defroutes routes
  (compojure/GET "/stops-in-rect" [] get-stops-in-rect-handler))

(defn- start-server! []
  (server/run-server
   (-> routes
       middleware.params/wrap-params)
   {:port 8080
    :join? false}))

(mount/defstate server
  :start (start-server!)
  :stop  (server :timeout 0))

