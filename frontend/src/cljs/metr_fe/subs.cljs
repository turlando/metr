(ns metr-fe.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::floating-card-page
 (fn [db _]
   (get-in db [:floating-card :page])))

(re-frame/reg-sub
 ::find-route-block-show-loading?
 (fn [db _]
   (get-in db [:floating-card :find-route-block :show-loading?])))

(re-frame/reg-sub
 ::find-route-block-result
 (fn [db _]
   (get-in db [:floating-card :find-route-block :result])))
