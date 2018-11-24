(ns metr-fe.state)

(def default-floating-card-current-page :main)

(def db
  {:floating-card {:page         default-floating-card-current-page
                   :find-route   {:query         ""
                                  :show-loading? false
                                  :results       []}
                   :route-detail {:route-id nil}}
   :map           {}})
