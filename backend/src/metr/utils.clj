(ns metr.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

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

(defn time->seconds [s]
  (let [t (string/split s #":")]
    (+ (* 3600 (Integer. (get t 0)))
       (* 60 (Integer. (get t 1)))
       (Integer. (get t 2)))))

(defn seconds->time [s]
  (let [hours   (quot s 3600)
        minutes (rem (quot s 60) 60)
        seconds (rem s 60)]
    (format "%02d:%02d:%02d" hours minutes seconds)))
