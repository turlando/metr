(ns metr-fe.state)

(def default-floating-card-current-page :main)

(def db
  {:floating-card {:page         default-floating-card-current-page
                   :find-route   {:query         nil
                                  :show-loading? false
                                  :results       []}
                   :route-detail {:route_id nil
                                  :route_code nil
                                  :route_name nil}}
   :map           {}})
