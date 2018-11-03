(ns metr.graph
  (:require [ubergraph.core :as ubergraph]))

(defn build-nodes [key xs]
  (->> xs
       (group-by key)
       (map (fn [[k v]]
              [k {}]
              #_[k (-> (first v)
                     (dissoc key))]))))

(defn build-relations [key xs props]
  (->> xs
       (partition 2 1)
       (map (fn [[x y]]
              (when (and (not (nil? x))
                         (not (nil? y)))
                [(get x key) (get y key) props])))))

(defn build-graph [nodes relations]
  (-> (ubergraph/multidigraph)
      (ubergraph/add-nodes-with-attrs* nodes)
      (ubergraph/add-directed-edges* relations)))
