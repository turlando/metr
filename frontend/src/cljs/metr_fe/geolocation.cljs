(ns metr-fe.geolocation
  (:require [re-frame.core :as re-frame]
            [metr-fe.events :as events]))

(defn hook-position! []
  (.getCurrentPosition
   js/navigator.geolocation.
   (fn [p]
     (re-frame/dispatch [::events/set-client-position
                         [(.-latitude js/p.coords)
                          (.-longitude js/p.coords)]]))))
