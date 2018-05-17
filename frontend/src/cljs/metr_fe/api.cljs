(ns metr-fe.api
  (:require [cljs-http.client :as http]
            [metr-fe.state :as state]))

(defn get-stops-in-rect [[lat-min lat-max
                          lon-min lon-max]]
  (http/get
   (str state/api-addr "stops-in-rect")
   {:with-credentials? false
    :query-params
    {"lat-min" lat-min
     "lat-max" lat-max
     "lon-min" lon-min
     "lon-max" lon-max}}))
