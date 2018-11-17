(ns metr-fe.components
  (:require [re-frame.core :as re-frame]
            [metr-fe.semantic-ui :as sui]
            [metr-fe.leaflet :as leaflet]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; BACKGROUND MAP                                                             ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn background-map []
  [leaflet/leafmap {:id                 "background-map"
                    :zoomControl        false
                    :attributionControl false
                    :center             leaflet/default-center
                    :zoom               leaflet/default-zoom}
   [leaflet/tile-layer {:url leaflet/tiles-url}]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FLOATING CARD MAIN BLOCKS                                                  ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn trip-card-block []
  [:> sui/card-content
   [:> sui/card-header
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
  [:> sui/card-content
   [:> sui/card-header
    "Fermate nelle vicinanze"]
   [:> sui/grid
    [:> sui/grid.Row {:centered true}
     [:> sui/button {:primary  true} "Tutte"]
     [:> sui/button "Bus"]
     [:> sui/button "Tram"]]]])

(defn find-line-block []
  [:> sui/card-content
   [:> sui/card-header
    "Trova linea"]
   [:> sui/search {:input       {:fluid true}
                   :placeholder "Linea"}]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FLOATING CARD PAGES                                                        ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti floating-card identity)

(defmethod floating-card :main []
  [:div {:id "floating-card-container"}
   [:> sui/card {:id     "floating-card"
                 :raised true}
    [trip-card-block]
    [nearby-stops-block]
    [find-line-block]]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; MAIN COMPONENT                                                             ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn main []
  (let [floating-card-page (re-frame/subscribe [:floating-card-page])]
    [:main
     [background-map]
     [floating-card @floating-card-page]]))
