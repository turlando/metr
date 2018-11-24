(ns metr-fe.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::floating-card-page
 (fn [db _]
   (get-in db [:floating-card :page])))

(re-frame/reg-sub
 ::find-route-show-loading?
 (fn [db _]
   (get-in db [:floating-card :find-route :show-loading?])))

(re-frame/reg-sub
 ::find-route-result
 (fn [db _]
   (get-in db [:floating-card :find-route :result])))

(re-frame/reg-sub
 ::route-detail-id
 (fn [db _]
   (get-in db [:floating-card :route-detail :route-id])))
