(ns metr.db-test
  (:require [clojure.test :refer [deftest testing is]]
            [metr.db :as sut]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]))

(deftest test-database-connection
  (testing "in-memory database creation and destruction"
    (let [conn (sut/open-in-memory-connection!)]
      (let [result   (jdbc/query conn ["SELECT 1 AS test"])
            expected '({:test 1})]
        (is (= expected result)))
      (sut/close-connection! conn)
      (is (thrown-with-msg?
           java.sql.SQLException #"database connection closed"
           (jdbc/query conn ["SELECT 1 AS test"])))))
  (testing "in filesystem database creation and destruction"
    (let [file (java.io.File/createTempFile "metr" "db")
          conn (sut/open-file-connection! file)]
      (let [result   (jdbc/query conn ["SELECT 1 AS test"])
            expected '({:test 1})]
        (is (= expected result)))
      (sut/close-connection! conn)
      (io/delete-file file)
      (is (thrown-with-msg?
           java.sql.SQLException #"database connection closed"
           (jdbc/query conn ["SELECT 1 AS test"]))))))
