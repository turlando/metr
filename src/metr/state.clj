(ns metr.state
  (:require [mount.core :as mount]
            [metr.db]))

(mount/defstate db
  :start {:connection (metr.db/get-connection)}
  :stop  (-> db :connection .close))
