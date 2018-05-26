(ns metr.gtfs
  (:require [clojure.set :refer [rename-keys]]
            [clojure.string :as string]
            [clojure.data.csv :as csv]
            [metr.utils :as utils]))

(defn- get-csv [path]
  (with-open [reader (utils/resource-reader path)]
    (->> (csv/read-csv reader)
         (utils/csv->maps)
         doall)))

(defn- clean-shapes [shape]
  (-> shape
      (rename-keys {:id_fermata       :id
                    :lat_fermata      :latitude
                    :lon_fermata      :longitude
                    :sequenza_fermate :sequence})
      (update :sequence #(Integer. %))
      (update :latitude #(Float. %))
      (update :longitude #(Float. %))))

(defn get-shapes []
  (->> (get-csv "gtfs/shapes.csv")
       (map clean-shapes)))

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

(defn- clean-timetable [timetable]
  (-> timetable
      (dissoc :departure_time
              :stop_headsign
              :drop_off_type
              :timepoint)
      (rename-keys {:arrival_time  :time
                    :stop_sequence :sequence})
      (update :sequence #(Integer. %))
      (update :time (if (string/blank? (:time timetable))
                      (constantly nil)
                      utils/time->seconds))))

(defn get-timetables []
  (->> (get-csv "gtfs/stoptimes.csv")
       (map clean-timetable)))
