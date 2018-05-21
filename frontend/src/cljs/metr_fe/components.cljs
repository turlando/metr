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
(def Rectangle (reagent/adapt-react-class react-leaflet/Rectangle))
(def Marker (reagent/adapt-react-class react-leaflet/Marker))
(def Popup (reagent/adapt-react-class react-leaflet/Popup))

(def map-marker-icon
  (js/L.icon #js {:iconUrl   (str "images/marker-icon.png")
                  :shadowUrl (str "images/marker-shadow.png")}))

(defn map-component []
  (let [viewport (re-frame/subscribe [::subs/map-viewport])
        stops    (re-frame/subscribe [::subs/stops-in-rect])
        rect     (when @viewport
                   (utils/coordinates->rect (.-center @viewport) 1))]

    (go (async/take!
         (apply api/get-stops-in-rect-in-time-by-stop-code
                (conj rect (utils/now) (utils/an-hour-from-now)))
         (fn [x]
           (re-frame/dispatch
            [::events/set-stops-in-rect (js->clj (:body x))]))))

    [Map
     {:className           "map-component"
      :viewport            @viewport
      :on-viewport-changed (fn [v]
                             (re-frame/dispatch
                              [::events/set-map-viewport v]))}
     [TileLayer
      {:url (str js/window.location.protocol
                 "//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png")}]
     (when rect
       [Rectangle {:bounds (js/L.latLngBounds
                            (js/L.latLng (get rect 0) (get rect 2))
                            (js/L.latLng (get rect 1) (get rect 3)))}])
     (when @stops
       (for [[stop times] @stops]
         (let [code (-> times first :stop_code)
               name (-> times first :stop_name)
               lat  (-> times first :stop_latitude)
               lon  (-> times first :stop_longitude)]
           ^{:key code}
           [Marker
            {:position (js/L.latLng lat lon)
             :icon     map-marker-icon}
            [Popup
             [:div
              [:p name " (" code ")"]
              (for [time times]
                ^{:key (:timetable_time time)}
                [:p (str (:route_code time) " - " (:timetable_time time))])]]])))]))
