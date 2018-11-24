(ns metr-fe.events
  (:require [clojure.string :as string]
            [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [re-frame-fx.dispatch]
            [metr-fe.state :as state]
            [metr-fe.api :as api]
            [ajax.core :as ajax]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   state/db))

(re-frame/reg-event-db
 ::floating-card-set-page
 (fn [db [_ v]]
   (assoc-in db [:floating-card :page] v)))

(re-frame/reg-event-fx
 ::find-route-set-query
 (fn [{:keys [db]} [k v]]
   {:db                (assoc-in db [:floating-card :find-route :query] v)
    :dispatch-debounce [{:id      k
                         :timeout 250
                         :action  :dispatch
                         :event   [::find-route-fetch]}]}))

(re-frame/reg-event-db
 ::find-route-set-loading
 (fn [db [_ v]]
   (assoc-in db [:floating-card :find-route :show-loading?] v)))

(re-frame/reg-event-fx
 ::find-route-fetch
 (fn [{:keys [db]} _]
   (let [query (get-in db [:floating-card :find-route :query])]
     (if (string/blank? query)
       {:db (assoc-in db [:floating-card :find-route :result] [])}
       {:dispatch   [::find-route-set-loading true]
        :http-xhrio {:uri             (api/url :routes query 5)
                     :method          :get
                     :timeout         1000
                     :response-format (ajax/json-response-format {:keywords? true})
                     :on-success      [::find-route-set-result]
                     :on-failure      nil}}))))

(re-frame/reg-event-fx
 ::find-route-set-result
 (fn [{:keys [db]} [_ result]]
   {:db       (assoc-in db [:floating-card :find-route :result] result)
    :dispatch [::find-route-set-loading false]}))

(re-frame/reg-event-fx
 ::floating-card-show-route-detail
 (fn [{:keys [db]} [_ v]]
   {:db       (assoc-in db [:floating-card :route-detail :route-id] v)
    :dispatch [::floating-card-set-page :route-detail]}))

