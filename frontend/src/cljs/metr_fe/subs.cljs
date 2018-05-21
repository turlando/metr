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
 ::map-bounds
 (fn [db _]
   (:map-bounds db)))

(re-frame/reg-sub
 ::map-stops
 (fn [db _]
   (:map-stops db)))
