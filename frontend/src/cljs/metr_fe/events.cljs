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

(re-frame/reg-event-fx
 ::find-route-block-set-query
 (fn [{:keys [db]} [k s]]
   {:db                (assoc-in db [:floating-card :find-route-block :query] s)
    :dispatch-debounce [{:id      k
                         :timeout 250
                         :action  :dispatch
                         :event   [::find-route-block-fetch]}]}))

(re-frame/reg-event-db
 ::find-route-block-set-loading
 (fn [db [_ v]]
   (assoc-in db [:floating-card :find-route-block :show-loading?] v)))

(re-frame/reg-event-fx
 ::find-route-block-fetch
 (fn [{:keys [db]} _]
   (let [query (get-in db [:floating-card :find-route-block :query])]
     (if (string/blank? query)
       {:db (assoc-in db [:floating-card :find-route-block :result] [])}
       {:dispatch   [::find-route-block-set-loading true]
        :http-xhrio {:uri             (api/url :routes query 5)
                     :method          :get
                     :timeout         1000
                     :response-format (ajax/json-response-format {:keywords? true})
                     :on-success      [::find-route-block-set-result]
                     :on-failure      nil}}))))

(re-frame/reg-event-fx
 ::find-route-block-set-result
 (fn [{:keys [db]} [_ result]]
   {:db       (assoc-in db [:floating-card :find-route-block :result] result)
    :dispatch [::find-route-block-set-loading false]}))
