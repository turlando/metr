(ns metr-fe.components
  (:require [clojure.set :as set]
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

(def screen-width
  (-> js/document .-documentElement .-clientWidth))

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
  (let [viewport    (re-frame/subscribe [::subs/map-viewport])
        stops       (re-frame/subscribe [::subs/map-stops])
        stop-data   (re-frame/subscribe [::subs/active-map-stop-data])]
    [Map
     {:className           "map-component"
      :viewport            @viewport
      :on-click            (fn []
                             (re-frame/dispatch
                              [::events/set-active-map-stop nil]))
      :on-viewport-changed (fn [v]
                             (re-frame/dispatch
                              [::events/set-map-viewport v])
                             nil)
      :on-move-end         (fn [e]
                             (re-frame/dispatch
                              [::events/set-map-bounds
                               (-> e .-target .getBounds bounds->rect)])
                             nil)
      :on-zoom-end         (fn [e]
                             (re-frame/dispatch
                              [::events/set-map-bounds
                               (-> e .-target .getBounds bounds->rect)])
                             nil)}
     [TileLayer
      {:url tiles-url}]
     (doall
      (for [stop @stops]
        ^{:key (-> stop :code)}
        [Marker
         {:on-click (fn []
                      (re-frame/dispatch
                       [::events/set-active-map-stop (-> stop :code)]))
          :position (js/L.latLng
                     (-> stop :latitude)
                     (-> stop :longitude))
          :icon     map-marker-icon}
         [Popup
          {:max-width screen-width}
          [:div.stop-popup
           [:header
            [:h1 (-> stop :name)]
            [:h2 (-> stop :code)]]
           (when @stop-data
             [:table
              [:tbody
               (for [d @stop-data]
                 ^{:key d}
                 [:tr
                  [:td (-> d :timetable_time)]
                  [:td
                   [:span.route-code (-> d :route_code)]
                   [:span.trip-destination (-> d :trip_destination)]]])]])]]]))]))
