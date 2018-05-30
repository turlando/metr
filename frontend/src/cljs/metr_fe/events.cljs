(ns metr-fe.events
  (:require [re-frame.core :as re-frame]
            [metr-fe.state :as state]
            [metr-fe.api :as api]
            [metr-fe.utils :as utils]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   state/db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 ::set-map-viewport
 (fn [db [_ viewport]]
   (assoc db :map-viewport viewport)))

(re-frame/reg-event-db
 ::set-map-stops
 (fn [db [_ stops]]
   (assoc db :map-stops stops)))

(re-frame/reg-event-db
 ::update-map-stops
 (fn [db [_ _]]
   (api/get-stops-in-rect!
    (-> db :map-bounds :min :latitude)
    (-> db :map-bounds :max :latitude)
    (-> db :map-bounds :min :longitude)
    (-> db :map-bounds :max :longitude)
    (fn [response]
      (re-frame/dispatch
       [::set-map-stops (-> response :body)])))
   db))

(re-frame/reg-event-fx
 ::set-map-bounds
 (fn [{:keys [db]}[_ bounds]]
   {:db       (assoc db :map-bounds bounds)
    :dispatch [::update-map-stops nil]}))

(re-frame/reg-event-db
 ::set-active-map-stop-data
 (fn [db [_ data]]
   (assoc db :active-map-stop-data data)))

(re-frame/reg-event-db
 ::update-active-map-stop-data
 (fn [db [_ code]]
   (if (nil? code)
     (re-frame/dispatch
      [::set-active-map-stop-data nil])
     (api/get-stop-times-by-stop-code!
      code
      (utils/now)
      (utils/an-hour-from-now)
      (fn [response]
        (re-frame/dispatch
         [::set-active-map-stop-data (-> response :body)]))))
   db))

(re-frame/reg-event-fx
 ::set-active-map-stop
 (fn [{:keys [db]} [_ code]]
   {:db       (assoc db :active-map-stop code)
    :dispatch [::update-active-map-stop-data code]}))
