(ns metr.gtfs
  (:require [clojure.data.csv :as csv]
            [clojure.set :refer [rename-keys]]
            [clojure.string :as string]
            [metr.utils :as utils]))

(defn- get-csv [path]
  (with-open [reader (utils/resource-reader path)]
    (->> (csv/read-csv reader)
         (utils/csv->maps)
         doall)))

(defn- clean-stop [stop]
  (-> stop
      (dissoc :stop_desc)
      (rename-keys {:stop_id   :id
                    :stop_code :code
                    :stop_name :name
                    :stop_lat  :latitude
                    :stop_lon  :longitude})
      (update :latitude #(Float. %))
      (update :longitude #(Float. %))))

(defn- get-stops []
  (->> (get-csv "gtfs/stops.csv")
       (map clean-stop)))

(defn- clean-route [route]
  (-> route
      (dissoc :agency_id
              :route_desc
              :route_url
              :route_color
              :route_text_color)
      (rename-keys {:route_id         :id
                    :route_short_name :code
                    :route_long_name  :name
                    :route_type       :type})))

(defn- get-routes []
  (->> (get-csv "gtfs/routes.csv")
       (map clean-route)))

(defn- clean-shape-points [point]
  (-> point
      (rename-keys {:id_fermata       :shape_id
                    :lat_fermata      :latitude
                    :lon_fermata      :longitude
                    :sequenza_fermate :sequence})
      (update :sequence #(Integer. %))
      (update :latitude #(Float. %))
      (update :longitude #(Float. %))))

(defn- add-distance-to-points-in-shape [points]
  "Given a list of shape points belonging to the same shape, this function
  assocs to each point the distance between itself and the previous point."
  (reduce (fn [acc x]
            (conj acc
                  (assoc x :distance
                         (if (empty? acc)
                           0.0
                           (utils/distance
                            (select-keys x [:latitude :longitude])
                            (select-keys (last acc) [:latitude :longitude]))))))
          [] points))

(defn- add-distance-to-shape-points [points]
  "Given a list of shape points belonging to different shapes, for each shape
  this function assocs to each point in the shape the distance between itself
  and the previous point."
  (->> points
       (group-by :shape_id)
       (utils/map-vals #(sort-by :sequence %))
       (utils/map-vals add-distance-to-points-in-shape)
       (reduce-kv (fn [s k v] (concat s v)) [])))

(defn- get-shape-points []
  (->> (get-csv "gtfs/shapes.csv")
       (map clean-shape-points)
       add-distance-to-shape-points))

(defn- clean-trip [trip]
  (-> trip
      (dissoc :trip_short_name
              :block_id)
      (rename-keys {:service_id    :service
                    :trip_id       :id
                    :trip_headsign :destination
                    :direction_id  :direction})))

(defn- get-trips []
  (->> (get-csv "gtfs/trips.csv")
       (map clean-trip)))

(defn- clean-stop-time [stop-time]
  (-> stop-time
      (dissoc :departure_time
              :stop_headsign
              :drop_off_type
              :timepoint)
      (rename-keys {:arrival_time  :time
                    :stop_sequence :sequence})
      (update :sequence #(Integer. %))
      (update :time (fn [x]
                      (if (string/blank? x)
                        nil
                        (utils/time->seconds x))))
      (update :distance (fn [x] nil))))

(defn- interpolate-time-in-stop-times-in-trip [stop-times]
  (let [first* (-> stop-times first :time)
        last*  (-> stop-times last :time)
        diff*  (- last* first*)
        count* (count stop-times)
        sample (quot diff* count*)]
    (map (fn [stop-time]
           (update stop-time :time
                   (fn [_]
                     (+ first*
                        (* sample (-> stop-time :sequence))))))
         stop-times)))

(defn- interpolate-time-in-stop-times [stop-times]
  (->> stop-times
      (group-by :trip_id)
      (utils/map-vals #(sort-by :sequence %))
      (utils/map-vals interpolate-time-in-stop-times-in-trip)
      (reduce-kv (fn [s k v] (concat s v)) [])))

(defn- get-stop-times []
  (->> (get-csv "gtfs/stoptimes.csv")
       (map clean-stop-time)
       interpolate-time-in-stop-times))

(defn build-dataset []
    {:stops        (get-stops)
     :routes       (get-routes)
     :shape-points (get-shape-points)
     :trips        (get-trips)
     :stop-times   (get-stop-times)})
