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

(defn get-shape-points []
  (->> (get-csv "gtfs/shapes.csv")
       (map clean-shape-points)
       add-distance-to-shape-points))

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

(defn get-stops []
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

(defn get-routes []
  (->> (get-csv "gtfs/routes.csv")
       (map clean-route)))

(defn- clean-trip [trip]
  (-> trip
      (dissoc :trip_short_name
              :block_id)
      (rename-keys {:service_id    :service
                    :trip_id       :id
                    :trip_headsign :destination
                    :direction_id  :direction})))

(defn get-trips []
  (->> (get-csv "gtfs/trips.csv")
       (map clean-trip)))

(defn- clean-stop-times [stop-time]
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
                        (utils/time->seconds x))))))

(defn- add-distance-to-stop-times [stops trips shape-points stop-times]
  "Given all the stop times, stops, trips and shape points in the dataset,
  for each trip this function will assoc to each stop the distance between
  itself and the previous stop, computed as a sum of the distance between
  the closest points to the shape associated to the trip."
  (let [stops-by-id              (->> stops
                                      (group-by :id)
                                      (reduce-kv (fn [m k v]
                                                   (assoc m k (first v))) {}))
        trips-by-id              (->> trips
                                      (group-by :id)
                                      (reduce-kv (fn [m k v]
                                                   (assoc m k (first v))) {}))
        shape-points-by-shape-id (->> shape-points
                                      (group-by :shape_id)
                                      (utils/map-vals #(sort-by :sequence %)))
        stop-times-by-trip-id    (->> stop-times
                                      (group-by :trip_id)
                                      (utils/map-vals #(sort-by :sequence %)))]
    (apply concat
           (for [[trip-id stop-times-in-trip] stop-times-by-trip-id]
             (let [shape-id         (-> (get trips-by-id trip-id) :shape_id)
                   points-in-shape  (get shape-points-by-shape-id shape-id)
                   stop-ids-in-trip (map :stop_id stop-times-in-trip)]
               (reduce
                (fn [new-stop-times stop-time]
                  (let [stop-id        (-> stop-time :stop_id)
                        stop           (get stops-by-id stop-id)
                        closest-points (->> (map
                                             (fn [point]
                                               (assoc point :distance*
                                                      (utils/distance
                                                       (select-keys stop [:latitude :longitude])
                                                       (select-keys point [:latitude :longitude]))))
                                             points-in-shape)
                                            (sort-by :distance*))
                        closest-point  (-> closest-points
                                           first
                                           (dissoc :distance*))
                        points-to-sum  (take-while
                                        (fn [point]
                                          (<= (:sequence point)
                                              (:sequence closest-point)))
                                        points-in-shape)
                        distance       (apply + (map :distance points-to-sum))
                        prev-distance  (if (empty? new-stop-times)
                                         0.0
                                         (apply + (map :distance new-stop-times)))]
                   (conj new-stop-times
                         (assoc stop-time :distance (- distance prev-distance)))))
                [] stop-times-in-trip))))))

(defn get-stop-times []
  (->> (get-csv "gtfs/stoptimes.csv")
       (map clean-stop-times)
       (add-distance-to-stop-times (get-stops) (get-trips) (get-shape-points))))
