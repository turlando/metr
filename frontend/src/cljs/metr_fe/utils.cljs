(ns metr-fe.utils
  (:require [goog.string :as gstring]
            [goog.string.format]))

(defn now []
  (let [d (js/Date.)]
    (gstring/format "%02d:%02d:%02d"
            (.getHours d)
            (.getMinutes d)
            (.getSeconds d))))

(defn an-hour-from-now []
  (let [d (js/Date.)]
    (gstring/format "%02d:%02d:%02d"
            (+ 1 (.getHours d))
            (.getMinutes d)
            (.getSeconds d))))
