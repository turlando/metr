(ns metr-fe.state)

(def default-floating-card-current-page :main)

(def db
  {:floating-card {:page            default-floating-card-current-page
                   :find-route-block {:query         ""
                                      :result        []
                                      :show-loading? false}}})
