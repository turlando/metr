(ns metr-fe.api)

(def base-url "http://localhost:8080/")

(defmulti url identity)
(defmethod url :routes [_ query limit]
  (str base-url "routes" "?q=" query "&limit=" limit))
