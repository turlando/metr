(ns metr-fe.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :floating-card-page
 (fn [db _]
   (get-in db [:floating-card :page])))
