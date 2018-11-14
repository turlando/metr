(ns metr-fe.components
  (:require [metr-fe.semantic-ui :as sui]))

(defn floating-card []
  [:div {:id "floating-card-container"}
   [:> sui/card {:id "floating-card"}
    [:> sui/card.Content
     [:p "Test"]]]])

(defn main []
  [:main
   [floating-card]])
