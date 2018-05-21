(ns metr-fe.utils)

(defn coordinates->rect [[lat lon] delta]
  (let [delta_lat_in_deg (* delta (/ 1 110.574))
        delta_lon_in_deg (* delta (/ 1 (* 111.320 (Math/cos lat))))
        half_lat_delta   (/ delta_lat_in_deg 2)
        half_lon_delta   (/ delta_lon_in_deg 2)]
    [(- lat half_lat_delta) (+ lat half_lat_delta)
     (- lon half_lon_delta) (+ lon half_lon_delta)]))

(defn now []
  (let [d (js/Date.)]
    (str (.getHours d) ":" (.getMinutes d) ":" (.getSeconds d))))

(defn an-hour-from-now []
  (let [d (js/Date.)]
    (str (+ 1 (.getHours d)) ":" (.getMinutes d) ":" (.getSeconds d))))
