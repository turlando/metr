(ns metr-fe.api)

(def base-url "http://localhost:8080/")

(defmulti url identity)
(defmethod url :routes [_ query]
  (str base-url "routes" "?q=" query))
