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

(defn- get-routes [request]
  (let [db-conn (-> request :db-conn)
        query   (-> request :params (get "q"))]
    (ok-response
     (api/get-routes db-conn query))))

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

(defn- get-stops-by-coordinates [request]
  (let [db-conn (-> request :db-conn)
        lat-min (-> request :params (get "lat-min"))
        lon-min (-> request :params (get "lon-min"))
        lat-max (-> request :params (get "lat-max"))
        lon-max (-> request :params (get "lon-max"))]
    (ok-response
     (api/get-stops-by-coordinates
      db-conn
      {:latitude-min  lat-min
       :longitude-min lon-min
       :latitude-max  lat-max
       :longitude-max lon-max}))))

(compojure/defroutes routes
  (compojure/GET "/routes" [] get-routes)
  (compojure/GET "/stop-times-by-stop-code" [] get-stop-times-by-stop-code)
  (compojure/GET "/stops-by-coordinates" [] get-stops-by-coordinates))

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
