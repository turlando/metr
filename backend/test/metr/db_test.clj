(ns metr.db-test
  (:require [clojure.test :refer [deftest testing is]]
            [metr.db :as sut]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]))

(deftest test-database-connection
  (let [query    "SELECT 1 AS test"
        expected '({:test 1})]
    (testing "in-memory database creation and destruction"
      (let [conn (sut/open-in-memory-connection!)]
        (is (= expected
               (jdbc/query conn query)))
        (sut/close-connection! conn)
        (is (thrown-with-msg?
             java.sql.SQLException #"database connection closed"
             (jdbc/query conn query)))))
    (testing "in filesystem database creation and destruction"
      (let [file (java.io.File/createTempFile "metr" "db")
            conn (sut/open-file-connection! file)]
        (is (= expected
               (jdbc/query conn query)))
        (sut/close-connection! conn)
        (io/delete-file file)
        (is (thrown-with-msg?
             java.sql.SQLException #"database connection closed"
             (jdbc/query conn query)))))))
