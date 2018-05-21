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
(def Popup (reagent/adapt-react-class react-leaflet/Popup))

(def tiles-url
  (str js/window.location.protocol
       "//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"))

(def map-marker-icon
  (js/L.icon #js {:iconUrl   (str "images/marker-icon.png")
                  :shadowUrl (str "images/marker-shadow.png")}))

(defn- bounds->rect [b]
  {:min {:latitude  (-> b .getSouthEast .-lat)
         :longitude (-> b .getNorthWest .-lng)}
   :max {:latitude  (-> b .getNorthWest .-lat)
         :longitude (-> b .getSouthEast .-lng)}})

(defn map-component []
  (let [viewport (re-frame/subscribe [::subs/map-viewport])
        bounds   (re-frame/subscribe [::subs/map-bounds])
        stops    (re-frame/subscribe [::subs/map-stops])]

    (when @bounds
      (go (async/take!
           (api/get-stops-in-rect-in-time-by-stop-code
            (-> @bounds :min :latitude)
            (-> @bounds :max :latitude)
            (-> @bounds :min :longitude)
            (-> @bounds :max :longitude)
            (utils/now)
            (utils/an-hour-from-now))
           (fn [x]
             (re-frame/dispatch
              [::events/set-map-stops (js->clj (:body x))])))))

    [Map
     {:className           "map-component"
      :viewport            @viewport
      :on-viewport-changed (fn [v]
                             (re-frame/dispatch
                              [::events/set-map-viewport v]))
      :on-move-end         (fn [e]
                             (re-frame/dispatch
                              [::events/set-map-bounds
                               (-> e .-target .getBounds bounds->rect)]))
      :on-zoom-end         (fn [e]
                             (re-frame/dispatch
                              [::events/set-map-bounds
                               (-> e .-target .getBounds bounds->rect)]))}

     [TileLayer
      {:url tiles-url}]


     (for [stop @stops]
       ^{:key (-> stop :stop :code)}
       [Marker
        {:position (js/L.latLng
                    (-> stop :stop :latitude)
                    (-> stop :stop :longitude))
         :icon     map-marker-icon}
        [Popup
         [:div
          [:p (-> stop :stop :name) " (" (-> stop :stop :code) ")"]
          (for [timetable (-> stop :timetable)]
            ^{:key timetable}
            [:p (-> timetable :route :code) " - "
             (-> timetable :route :name) " - "
             (-> timetable :time)])]]])]))
