(ns metr-fe.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [metr-fe.state :as state]
            [metr-fe.api :as api]
            [ajax.core :as ajax]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   state/db))

(re-frame/reg-event-fx
 ::find-route-query
 (fn [{:keys [db]} [_ query]]
   {:db (-> db
            (assoc-in [:floating-card :find-route-block :query] query)
            (assoc-in [:floating-card :find-route-block :show-loading?] true))
    :http-xhrio {:uri (api/url :routes query)
                 :method :get
                 :timeout 1000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::find-route-query-result]
                 :on-failure nil}}))

(re-frame/reg-event-db
 ::find-route-query-result
 (fn [db [_ result]]
   (-> db
       (assoc-in [:floating-card :find-route-block :result] result)
       (assoc-in [:floating-card :find-route-block :show-loading?] false))))
