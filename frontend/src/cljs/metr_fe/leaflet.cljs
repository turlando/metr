(ns metr-fe.leaflet
  (:require [reagent.core :as reagent]
            [react-leaflet :as react-leaflet]))

(def tiles-url
  (str js/window.location.protocol
       "//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"))

(def default-center [38.1940 15.5504])
(def default-zoom 13)

(def geomap (reagent/adapt-react-class react-leaflet/Map))
(def tile-layer (reagent/adapt-react-class react-leaflet/TileLayer))
