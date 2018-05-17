(ns metr-fe.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [metr-fe.events :as events]
            [metr-fe.subs :as subs]
            [metr-fe.routes :as routes]
            [metr-fe.panels :as panels]))

(def debug?
  ^boolean goog.DEBUG)

(defn- maybe-setup-dev! []
  (when debug?
    (enable-console-print!)))

(defn- mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [panels/root]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (maybe-setup-dev!)
  (mount-root))
