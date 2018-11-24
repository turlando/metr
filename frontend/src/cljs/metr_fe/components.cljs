(ns metr-fe.components
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [antizer.reagent :as ant]
            [metr-fe.events :as events]
            [metr-fe.subs :as subs]
            [metr-fe.leaflet :as leaflet]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ICONS                                                                      ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def search-icon  [ant/icon {:type "search"}])
(def loading-icon [ant/icon {:type "loading"}])


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
;; FLOATING CARD MAIN PAGE BLOCKS                                             ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn trip-card-block []
  [:div {:class "card-content"}
   [:h2 "Dove andiamo?"]
   [ant/form
    [ant/input {:placeholder "Da"}]
    [ant/input {:placeholder "A"}]
    [ant/button {:type  "primary"
                 :block true}
     "Vai"]]])

(defn nearby-stops-block []
  [:div {:class "card-content"}
   [:h2 "Fermate nelle vicinanze"]
   [ant/button-group
    [ant/button {:type "primary"} "Tutte"]
    [ant/button "Bus"]
    [ant/button "Tram"]]])

(defn find-route-block-option [r]
  [ant/auto-complete-option
   {:class "find-route-block-option"
    :key   (:route_id r)
    :text  (:route_id r)}
   [:span {:class "route-code"} (:route_code r)]
   [:span {:class "route-name"} (:route_name r)]])

(defn find-route-block []
  (let [show-loading? (re-frame/subscribe
                       [::subs/find-route-show-loading?])
        result        (re-frame/subscribe
                       [::subs/find-route-result])
        handle-result (fn [r]
                        (str (:route_code r) " - " (:route_name r)))]
    [:div {:class "card-content"}
     [:h2 "Trova linea"]
     [ant/auto-complete
      {:class       "max-width"
       :placeholder "Linea"
       :filter-option false
       :suffix      (reagent/as-element (if @show-loading?
                                          loading-icon
                                          search-icon))
       :on-change   #(re-frame/dispatch [::events/find-route-set-query %])
       :on-select   #(re-frame/dispatch [::events/floating-card-show-route-detail %1])}
      (for [r @result]
        (find-route-block-option r))]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FLOATING CARD PAGES                                                        ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti floating-card identity)

(defmethod floating-card :main []
  [:div {:id "floating-card-container"}
   [ant/card {:id "floating-card"}
    [trip-card-block]
    [ant/divider]
    [nearby-stops-block]
    [ant/divider]
    [find-route-block]]])

(defmethod floating-card :route-detail []
  (let [route-id (re-frame/subscribe [::subs/route-detail-id])]
    [:div {:id "floating-card-container"}
     [ant/card {:id "floating-card"}
      [:h2 (str "Linea " @route-id)]]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; MAIN COMPONENT                                                             ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn main []
  (let [floating-card-page (re-frame/subscribe [::subs/floating-card-page])]
    [:main
     [background-map]
     [floating-card @floating-card-page]]))
