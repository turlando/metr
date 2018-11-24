(ns metr.utils
  (:require [clojure.string :as string]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [geo.spatial :as spatial]
            [clojure.java.jdbc :as jdbc]))

(defn read-int [s]
  (Integer/parseInt (re-find #"^-?\d+$" s)))

(defn read-float [s]
  (Float/parseFloat (re-find #"^-?\d+\.\d+$" s)))

(defn map-vals [f m]
  (zipmap (keys m)
          (map f (vals m))))

(defn remove-nil-vals [m]
  (into {} (remove (comp nil? second) m)))

(defn slurp-resource [path]
  (-> path
      io/resource
      slurp))

(defn ^java.io.Reader resource-reader [path]
  (-> path
      io/resource
      io/reader))

(defn csv->maps [csv]
  (map zipmap
       (->> (first csv)
            (map keyword)
            repeat)
       (rest csv)))

(defn read-csv [path]
  (with-open [reader (resource-reader path)]
    (->> (csv/read-csv reader)
         csv->maps
         doall)))

(defn sql-query-with-named-params->sql-query [query params]
  (let [pattern    #":[\w_?]+"
        keys-in-q  (map #(keyword (string/replace % #"^:" ""))
                        (re-seq pattern query))
        new-query  (string/replace query pattern "?")
        new-params (map #(get params %) keys-in-q)]
    (concat [new-query] new-params)))

(defn time->seconds [s]
  (let [t (string/split s #":")
        h (get t 0)
        m (get t 1)
        s (get t 2)]
    (+ (* 3600 (read-int h))
       (* 60 (read-int m))
       (read-int s))))

(defn seconds->time [s]
  (let [hours   (quot s 3600)
        minutes (rem (quot s 60) 60)
        seconds (rem s 60)]
    (format "%02d:%02d:%02d" hours minutes seconds)))

(defn distance [from to]
  (let [from* (spatial/spatial4j-point (:latitude from) (:longitude from))
        to*   (spatial/spatial4j-point (:latitude to) (:longitude to))]
    (spatial/distance from* to*)))
