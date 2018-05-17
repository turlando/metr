(ns metr-fe.components
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [react-leaflet :as leaflet]
            [metr-fe.subs :as subs]
            [metr-fe.events :as events]
            [metr-fe.state :as state]
            [metr-fe.api :as api]
            [metr-fe.utils :as utils]))

(def Map (reagent/adapt-react-class react-leaflet/Map))
(def TileLayer (reagent/adapt-react-class react-leaflet/TileLayer))
(def Marker (reagent/adapt-react-class react-leaflet/Marker))

(def map-marker-icon
  (js/L.icon #js {:iconUrl   (str "images/marker-icon.png")
                  :shadowUrl (str "images/marker-shadow.png")}))

(defn map-component []
  (let [viewport (re-frame/subscribe [::subs/map-viewport])
        stops    (re-frame/subscribe [::subs/stops-in-rect])
        rect     (when @viewport
                   (utils/coordinates->rect (.-center @viewport) 10))]

    (go (async/take!
         (api/get-stops-in-rect rect)
         (fn [x]
           (re-frame/dispatch
            [::events/set-stops-in-rect (:body x)]))))

    [Map
     {:className           "map-component"
      :viewport            @viewport
      :on-viewport-changed (fn [v]
                             (re-frame/dispatch
                              [::events/set-map-viewport v]))}

     [TileLayer
      {:url (str js/window.location.protocol
                 "//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png")}]
     (when @stops
       (map (fn [s]
              ^{:key s}
              [Marker
               {:position (js/L.latLng (:latitude s) (:longitude s))
                :icon     map-marker-icon}])
            @stops))]))
