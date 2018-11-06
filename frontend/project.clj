(defproject metr-fe "0.1.0-SNAPSHOT"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"
  :plugins [[lein-cljsbuild "1.1.7"]
            [deraen/lein-less4j "0.6.2"]]

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.8.0"]
                 [re-frame "0.10.5"]
                 [secretary "1.2.3"]
                 [cljs-http "0.1.45"]
                 [cljsjs/react-leaflet "1.6.5-0"]]

  :source-paths ["src/clj/dev"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "resources/public/css"
                                    "target"]

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :figwheel {:server-port   8081
             :css-dirs      ["resources/public/css"]}

  :less {:source-paths ["src/less"]
         :target-path  "resources/public/css"}

  :profiles
  {:dev  {:plugins      [[lein-figwheel "0.5.16"]
                         [lein-doo "0.1.10"]]
          :dependencies [[binaryage/devtools "0.9.10"]
                         [figwheel-sidecar "0.5.16"]
                         [com.cemerick/piggieback "0.2.2"]]
          :less         {:source-map true}}
   :prod {}}

  :aliases {"dev"   ["with-profile" "+dev" "do"
                     ["clean"]
                     ["less4j" "once"]
                     ["figwheel" "dev"]]
            "build" ["with-profile" "+prod,-dev" "do"
                     ["clean"]
                     ["less4j" "once"]
                     ["cljsbuild" "once" "min"]]}

  :cljsbuild {:builds
              [{:id           "dev"
                :source-paths ["src/cljs"]
                :figwheel     {:websocket-url "ws://[[server-hostname]]:[[server-port]]/figwheel-ws"
                               :on-jsload "metr-fe.core/mount-root"}
                :compiler     {:main                 metr-fe.core
                               :output-to            "resources/public/js/compiled/app.js"
                               :output-dir           "resources/public/js/compiled/out"
                               :asset-path           "js/compiled/out"
                               :source-map-timestamp true
                               :preloads             [devtools.preload]
                               :external-config      {:devtools/config {:features-to-install :all}}}}
               {:id           "min"
                :source-paths ["src/cljs"]
                :compiler     {:main            metr-fe.core
                               :output-to       "resources/public/js/compiled/app.js"
                               :optimizations   :advanced
                               :closure-defines {goog.DEBUG false}
                               :pretty-print    false}}]})
