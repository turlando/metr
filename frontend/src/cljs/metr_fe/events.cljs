(ns metr-fe.events
  (:require [re-frame.core :as re-frame]
            [metr-fe.state :as state]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   state/db))
