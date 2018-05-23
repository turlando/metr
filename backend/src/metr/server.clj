(ns metr.server
  (:require [clojure.data.json :as json]
            [mount.core :as mount]
            [org.httpkit.server :as server]
            [ring.middleware.params :as middleware.params]
            [compojure.core :as compojure]
            [metr.api :as api]))

(defn- json-payload [payload]
  {:status  200
   :headers {"Content-Type"                "application/json; charset=utf-8"
             "Access-Control-Allow-Origin" "http://localhost:8081"}
   :body    (-> payload
                json/write-str)})

(defn- get-stops-in-rect-handler [request]
  (let [lat-min (-> request :params (get "lat-min"))
        lat-max (-> request :params (get "lat-max"))
        lon-min (-> request :params (get "lon-min"))
        lon-max (-> request :params (get "lon-max"))]
    (json-payload
     (api/get-stops-in-rect
      lat-min lat-max
      lon-min lon-max))))

(defn- get-timetable-in-rect-in-time-handler [request]
  (let [lat-min (-> request :params (get "lat-min"))
        lat-max (-> request :params (get "lat-max"))
        lon-min (-> request :params (get "lon-min"))
        lon-max (-> request :params (get "lon-max"))
        time-min (-> request :params (get "time-min"))
        time-max (-> request :params (get "time-max"))]
    (json-payload
     (api/get-timetable-in-rect-in-time
      lat-min lat-max
      lon-min lon-max
      time-min time-max))))

(compojure/defroutes routes
  (compojure/GET "/stops-in-rect" [] get-stops-in-rect-handler)
  (compojure/GET "/timetable-in-rect-in-time" [] get-timetable-in-rect-in-time-handler))

(defn- start-server! []
  (server/run-server
   (-> routes
       middleware.params/wrap-params)
   {:port 8080
    :join? false}))

(mount/defstate server
  :start (start-server!)
  :stop  (server :timeout 0))
