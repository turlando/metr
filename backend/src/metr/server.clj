(ns metr.server
  (:require [clojure.data.json :as json]
            [compojure.core :as compojure]
            [metr.api :as api]
            [org.httpkit.server :as server]
            [ring.middleware.params :as middleware.params]
            [ring.middleware.json :as middleware.json]))

(defn- ok-response [body]
  {:status  200
   :body    body})

(defn- get-stop-times-by-stop-code [request]
  (let [db-conn  (-> request :db-conn)
        code     (-> request :params (get "code"))
        time-min (-> request :params (get "time-min"))
        time-max (-> request :params (get "time-max"))]
    (ok-response
     (api/get-stop-times-by-stop-code
      db-conn
      code
      time-min time-max))))

(defn- get-stops-in-rect-handler [request]
  (let [db-conn (-> request :db-conn)
        lat-min (-> request :params (get "lat-min"))
        lat-max (-> request :params (get "lat-max"))
        lon-min (-> request :params (get "lon-min"))
        lon-max (-> request :params (get "lon-max"))]
    (ok-response
     (api/get-stops-in-rect
      db-conn
      lat-min lat-max
      lon-min lon-max))))

(compojure/defroutes routes
  (compojure/GET "/stop-times-by-stop-code" [] get-stop-times-by-stop-code)
  (compojure/GET "/stops-in-rect" [] get-stops-in-rect-handler))

(defn- cors-response [response]
  (assoc-in response [:headers "Access-Control-Allow-Origin"] "*"))

(defn- wrap-cors [handler]
  (fn wrap-cors*
    ([request]
     (-> (handler request) cors-response))
    ([request respond raise]
     (handler request #(respond (cors-response %)) raise))))

(defn- wrap-db-conn [handler db-conn]
  (fn wrap-db-conn*
    ([request]
     (handler (assoc request :db-conn db-conn)))
    ([request respond raise]
     (handler (assoc request :db-conn db-conn) #(respond %) raise))))

(defn start!
  [{:keys [db-conn]
    :as args}]
  (server/run-server
   (-> routes
       (wrap-db-conn db-conn)
       middleware.params/wrap-params
       middleware.json/wrap-json-response
       wrap-cors)
   {:port  8080
    :join? false}))

(defn stop! [s]
  (s :timeout 0))
