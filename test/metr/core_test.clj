(ns metr.core-test
  (:require [clojure.test :refer :all]
            [metr.core :refer :all]))

(deftest noop
  (testing "I always succeed, but I also do very little."
    (is true)))
