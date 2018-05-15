(defproject metr "0.1.0-SNAPSHOT"
  :description ""
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [org.xerial/sqlite-jdbc "3.21.0.1"]
                 [mount "0.1.12"]
                 [clj-http "2.3.0"]]
  :main ^:skip-aot metr.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
