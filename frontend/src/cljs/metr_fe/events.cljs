(ns metr-fe.events
  (:require [re-frame.core :as re-frame]
            [metr-fe.state :as state]))

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
 ::set-stops-in-rect
 (fn [db [_ stops-in-rect ]]
   (assoc db :stops-in-rect stops-in-rect)))
