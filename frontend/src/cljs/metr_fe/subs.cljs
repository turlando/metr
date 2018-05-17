(ns metr-fe.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::map-viewport
 (fn [db _]
   (:map-viewport db)))

(re-frame/reg-sub
 ::stops-in-rect
 (fn [db _]
   (:stops-in-rect db)))
