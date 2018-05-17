(ns metr-fe.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [metr-fe.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
