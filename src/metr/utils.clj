(ns metr.utils
  (:require [clojure.java.io :as io]))

(defn slurp-resource [path]
  (-> path
      io/resource
      slurp))

(defn resource-reader [path]
  (-> path
      io/resource
      io/reader))

(defn csv->maps [csv]
  (map zipmap
       (->> (first csv)
            (map keyword)
            repeat)
       (rest csv)))
