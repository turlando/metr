(defproject metr "0.1.0-SNAPSHOT"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [org.xerial/sqlite-jdbc "3.21.0.1"]
                 [mount "0.1.12"]
                 [clj-http "2.3.0"]
                 [http-kit "2.2.0"]
                 [ring/ring-core "1.6.3"]
                 [compojure "1.6.1"]
                 [factual/geo "1.2.0"]]

  :main ^:skip-aot metr.core

  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
