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

(defn get-stops-in-rect-in-time-by-stop-code
  [lat-min lat-max
   lon-min lon-max
   time-min time-max]
  (http/get
   (str state/api-addr "timetable-in-rect-in-time")
   {:with-credentials? false
    :query-params
    {"lat-min"  lat-min
     "lat-max"  lat-max
     "lon-min"  lon-min
     "lon-max"  lon-max
     "time-min" time-min
     "time-max" time-max}}))
