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
  (let [viewport      (re-frame/subscribe [::subs/map-viewport])
        stops         (re-frame/subscribe [::subs/map-stops])]

    (reagent/create-class
     {:display-name "map-component"

      :component-will-update
      (fn []
        nil)

      :reagent-render
      (fn []
        [Map
         {:className           "map-component"
          :viewport            @viewport
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
         (for [stop @stops]
           ^{:key (-> stop :code)}
           [Marker
            {:position (js/L.latLng
                        (-> stop :latitude)
                        (-> stop :longitude))
             :icon     map-marker-icon}])])})))
