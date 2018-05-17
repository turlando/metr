(ns metr-fe.panels
  (:require [re-frame.core :as re-frame]
            [metr-fe.subs :as subs]
            [metr-fe.components :as components]))

(defn home-panel []
  [components/map-component])

(defn- show-panel [panel-name]
  (case panel-name
    :home [home-panel]
    [:div]))

(defn root []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
