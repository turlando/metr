(ns metr.geocoder
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]))

(defn address->coordinates [address]
  (-> (client/get
       "https://nominatim.openstreetmap.org/search/"
       {:headers      {:accept-language "it-it"}
        :query-params {:format          "jsonv2"
                       :accept-language "it-it"
                       :country         "italy"
                       :city            "messina"
                       :street          address}})
      :body
      (json/read-str :key-fn keyword)
      first
      (map [:lat :lon])))
