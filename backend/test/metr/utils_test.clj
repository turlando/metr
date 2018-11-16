(ns metr.utils-test
  (:require [clojure.test :refer [deftest testing is]]
            [metr.utils :as sut]))

(deftest test-sql-query-with-named-params->sql-query
  (let [f sut/sql-query-with-named-params->sql-query]
    (testing "distinct params"
      (let [in  ["SELECT a FROM A WHERE a = :a" {:a 1}]
            out ["SELECT a FROM A WHERE a = ?" 1]]
        (is (= (apply f in) out)))
      (let [in  ["SELECT a FROM A WHERE a = :a AND b = :b" {:a 1 :b 2}]
            out ["SELECT a FROM A WHERE a = ? AND b = ?" 1 2]]
        (is (= (apply f in) out))))
    (testing "repeated params"
      (let [in  ["SELECT a FROM A WHERE a = :a AND a = :a" {:a 1}]
            out ["SELECT a FROM A WHERE a = ? AND a = ?" 1 1]]
        (is (= (apply f in) out)))
      (let [in  ["SELECT a FROM A WHERE a = :a AND b = :b OR :a = :b" {:a 1 :b 2}]
            out ["SELECT a FROM A WHERE a = ? AND b = ? OR ? = ?" 1 2 1 2]]
        (is (= (apply f in) out))))
    (testing "missing params"
      (let [in  ["SELECT a FROM A WHERE a = :a" {}]
            out ["SELECT a FROM A WHERE a = ?" nil]]
        (is (= (apply f in) out)))
      (let [in  ["SELECT a FROM A WHERE a = :a AND b = :b OR :a = :b" {:a 1}]
            out ["SELECT a FROM A WHERE a = ? AND b = ? OR ? = ?" 1 nil 1 nil]]
        (is (= (apply f in) out))))
    (testing "valid param names"
      (let [in  ["SELECT a FROM A WHERE a = :a_one" {:a_one 1}]
            out ["SELECT a FROM A WHERE a = ?" 1]]
        (is (= (apply f in) out)))
      (let [in  ["SELECT a FROM A WHERE a = :a_1" {:a_1 1}]
            out ["SELECT a FROM A WHERE a = ?" 1]]
        (is (= (apply f in) out)))
      (let [in  ["SELECT a FROM A WHERE a = :?" {:? 1}]
            out ["SELECT a FROM A WHERE a = ?" 1]]
        (is (= (apply f in) out)))
      (let [in  ["SELECT a FROM A WHERE a = :? AND b = :?" {:? 1}]
            out ["SELECT a FROM A WHERE a = ? AND b = ?" 1 1]]
        (is (= (apply f in) out))))))
