(ns metr-fe.components
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [antizer.reagent :as ant]
            [metr-fe.leaflet :as leaflet]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ICONS                                                                      ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def search-icon [ant/icon {:type "search"}])


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
  [:div {:class "card-content"}
   [:h2 "Dove andiamo?"]
   [ant/form
    [ant/input {:placeholder "Da"}]
    [ant/input {:placeholder "A"}]
    [ant/button {:type "primary"
                 :block true}
     "Vai"]]])

(defn nearby-stops-block []
  [:div {:class "card-content"}
   [:h2 "Fermate nelle vicinanze"]
   [ant/button-group
    [ant/button {:type "primary"} "Tutte"]
    [ant/button "Bus"]
    [ant/button "Tram"]]])

(defn find-line-block []
  [:div {:class "card-content"}
   [:h2 "Trova linea"]
   [ant/input {:placeholder "Linea"
               :suffix (reagent/as-element search-icon)}]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FLOATING CARD PAGES                                                        ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti floating-card identity)

(defmethod floating-card :main []
  [:div {:id "floating-card-container"}
   [ant/card {:id     "floating-card"}
    [trip-card-block]
    [ant/divider]
    [nearby-stops-block]
    [ant/divider]
    [find-line-block]]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; MAIN COMPONENT                                                             ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn main []
  (let [floating-card-page (re-frame/subscribe [:floating-card-page])]
    [:main
     [background-map]
     [floating-card @floating-card-page]]))
