(defproject metr "0.1.0-SNAPSHOT"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.xerial/sqlite-jdbc "3.25.2"]
                 [commons-io/commons-io "2.6"]
                 [clj-http "2.3.0"]
                 [http-kit "2.3.0"]
                 [ring/ring-core "1.7.1" :exclusions [commons-io commons-codec]]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.6.1"]
                 [factual/geo "1.2.0" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [ubergraph "0.5.2"]]

  :main ^:skip-aot metr.core

  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
