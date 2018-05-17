(ns metr-fe.utils)

(defn coordinates->rect [[lat lon] delta]
  (let [delta_lat_in_deg (* delta (/ 1 110.574))
        delta_lon_in_deg (/ 1 (* 111.320 (Math/cos lat)))
        half_lat_delta   (/ delta_lat_in_deg 2)
        half_lon_delta   (/ delta_lon_in_deg 2)]
    [(- lat half_lat_delta) (+ lat half_lat_delta)
     (- lon half_lon_delta) (+ lon half_lon_delta)]))
