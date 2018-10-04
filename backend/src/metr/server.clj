(ns metr.server
  (:require [clojure.data.json :as json]
            [compojure.core :as compojure]
            [metr.api :as api]
            [mount.core :as mount]
            [org.httpkit.server :as server]
            [ring.middleware.params :as middleware.params]))

(defn- json-payload [payload]
  {:status  200
   :headers {"Content-Type"                "application/json; charset=utf-8"
             "Access-Control-Allow-Origin" "*"}
   :body    (-> payload
                json/write-str)})

(defn- get-stop-times-by-stop-code [request]
  (let [code     (-> request :params (get "code"))
        time-min (-> request :params (get "time-min"))
        time-max (-> request :params (get "time-max"))]
    (json-payload
     (api/get-stop-times-by-stop-code
      code
      time-min time-max))))

(defn- get-stops-in-rect-handler [request]
  (let [lat-min (-> request :params (get "lat-min"))
        lat-max (-> request :params (get "lat-max"))
        lon-min (-> request :params (get "lon-min"))
        lon-max (-> request :params (get "lon-max"))]
    (json-payload
     (api/get-stops-in-rect
      lat-min lat-max
      lon-min lon-max))))

(compojure/defroutes routes
  (compojure/GET "/stop-times-by-stop-code" [] get-stop-times-by-stop-code)
  (compojure/GET "/stops-in-rect" [] get-stops-in-rect-handler))

(defn- start-server! []
  (server/run-server
   (-> routes
       middleware.params/wrap-params)
   {:port  8080
    :join? false}))

(defn register-mount! []
  (mount/defstate server
    :start (start-server!)
    :stop  (server :timeout 0)))
