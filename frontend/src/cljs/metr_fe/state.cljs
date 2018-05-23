(ns metr-fe.state)

(def api-addr
  "http://127.0.0.1:8080/")

(def default-viewport
  {:center [38.1940 15.5504]
   :zoom   14})

(def db
  {:map-viewport    default-viewport
   :map-bounds      {}
   :map-stops       []})
