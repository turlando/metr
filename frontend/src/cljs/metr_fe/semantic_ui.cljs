(ns metr-fe.semantic-ui
  (:require [cljsjs.semantic-ui-react]
            [goog.object]))

(defn- component
  "Get a component from sematic-ui-react:
    (component \"Button\")
    (component \"Menu\" \"Item\")"
  [k & ks]
  (if (seq ks)
    (apply goog.object/getValueByKeys js/semanticUIReact k ks)
    (goog.object/get js/semanticUIReact k)))

(def card      (component "Card"))
(def grid      (component "Grid"))
