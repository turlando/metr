(ns metr-fe.components
  (:require [metr-fe.semantic-ui :as sui]
            [metr-fe.leaflet :as leaflet]))

(defn background-map []
  [leaflet/leafmap {:id                 "background-map"
                    :zoomControl        false
                    :attributionControl false
                    :center             leaflet/default-center
                    :zoom               leaflet/default-zoom}
   [leaflet/tile-layer {:url leaflet/tiles-url}]])

(defn trip-card-block []
  [:> sui/card.Content
   [:> sui/card.Header
    "Dove andiamo?"]
   [:> sui/form
    [:> sui/input {:fluid       true
                   :placeholder "Da"}]
    [:> sui/input {:fluid       true
                   :placeholder "A"}]
    [:> sui/button {:primary true
                    :floated "right"}
     "Vai"]]])

(defn nearby-stops-block []
  [:> sui/card.Content
   [:> sui/card.Header
    "Fermate nelle vicinanze"]
   [:> sui/grid
    [:> sui/grid.Row {:centered true}
     [:> sui/button {:primary true} "Tutte"]
     [:> sui/button "Bus"]
     [:> sui/button "Tram"]]]])

(defn find-line-block []
  [:> sui/card.Content
   [:> sui/card.Header
    "Trova linea"]
   [:> sui/input {:fluid       true
                  :placeholder "Linea"}]])

(defn floating-card []
  [:div {:id "floating-card-container"}
   [:> sui/card {:id     "floating-card"
                 :raised true}
    [trip-card-block]
    [nearby-stops-block]
    [find-line-block]]])

(defn main []
  [:main
   [background-map]
   [floating-card]])
