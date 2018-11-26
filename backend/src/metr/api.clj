(ns metr.api
  (:require [metr.db :as db]
            [metr.utils :as utils]))

(defn get-routes
  [& args]
  (apply db/query-routes-by-code-or-name args))
