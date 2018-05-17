(ns metr-fe.state)

(def default-map-viewport
  {:center [38.1940 15.5504]
   :zoom   14})

(def api-addr
  "http://127.0.0.1:8080/")

(def db
  {:map-viewport default-map-viewport
   :stops-in-rect []})
