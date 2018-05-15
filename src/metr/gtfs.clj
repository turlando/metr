(ns metr.gtfs
  (:require [clojure.set :refer [rename-keys]]
            [clojure.data.csv :as csv]
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
                    :stop_lon  :longitude})))

(defn get-stops []
  (->> (get-csv "gtfs/stops.csv")
       (map clean-stop)))

(defn- clean-route [route]
  (-> route
      (dissoc :agency_id)
      (dissoc :route_desc)
      (dissoc :route_url)
      (dissoc :route_color)
      (dissoc :route_text_color)
      (rename-keys {:route_id         :id
                    :route_short_name :code
                    :route_long_name  :name
                    :route_type       :type})))

(defn get-routes []
  (->> (get-csv "gtfs/routes.csv")
       (map clean-route)))

(defn- clean-trip [trip]
  (-> trip
      (dissoc :trip_short_name)
      (dissoc :shape_id)
      (dissoc :block_id)
      (rename-keys {:route_id      :route_id
                    :service_id    :service
                    :trip_id       :id
                    :trip_headsign :destination
                    :direction_id  :direction})))

(defn get-trips []
  (->> (get-csv "gtfs/trips.csv")
       (map clean-trip)))

(defn- clean-timetable [timetable]
  (-> timetable
      (dissoc :departure_time)
      (dissoc :stop_headsign)
      (dissoc :drop_off_type)
      (dissoc :timepoint)
      (rename-keys {:arrival_time  :time
                    :stop_sequence :sequence})))

(defn get-timetables []
  (->> (get-csv "gtfs/stoptimes.csv")
       (map clean-timetable)))
