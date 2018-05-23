(ns metr-fe.events
  (:require [re-frame.core :as re-frame]
            [metr-fe.state :as state]
            [metr-fe.api :as api]))

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
 ::update-stops
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
 (fn [{:keys [db] }[_ bounds]]
   {:db       (assoc db :map-bounds bounds)
    :dispatch [::update-stops]}))
