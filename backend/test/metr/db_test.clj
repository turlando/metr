(ns metr.db-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.java.jdbc :as jdbc]
            [metr.db :as sut]))

(deftest test-database-connection
  (testing "in-memory database creation and destruction"
    (let [conn (sut/open-in-memory-connection!)]
      (let [result   (jdbc/query conn ["SELECT 1 AS test"])
            expected '({:test 1})]
        (is (= expected result)))
      (sut/close-connection! conn)
      (is (thrown-with-msg?
           java.sql.SQLException #"database connection closed"
           (jdbc/query conn ["SELECT 1 AS test"]))))))
