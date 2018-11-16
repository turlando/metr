(ns metr-fe.state)

(def default-floating-card-current-page :main)

(def db
  {:floating-card {:page            default-floating-card-current-page
                   :find-line-block {:show-loading? false}}})
