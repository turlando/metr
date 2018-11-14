(ns metr-fe.components
  (:require [metr-fe.semantic-ui :as sui]
            [metr-fe.leaflet :as leaflet]))

(defn background-map []
  [leaflet/geomap {:id     "background-map"
                   :center leaflet/default-center
                   :zoom   leaflet/default-zoom}
   [leaflet/tile-layer {:url leaflet/tiles-url}]])

(defn floating-card []
  [:div {:id "floating-card-container"}
   [:> sui/card {:id     "floating-card"
                 :raised true}
    [:> sui/card.Content
     [:p "Test"]]]])

(defn main []
  [:main
   [background-map]
   [floating-card]])
