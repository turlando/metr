(defproject metr-fe "0.1.0-SNAPSHOT"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.8.0"
  :plugins [[lein-cljsbuild "1.1.7"]
            [deraen/lein-less4j "0.6.2"]]

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.439"
                  :exclusions [[com.google.errorprone/error_prone_annotations]
                               [com.google.code.findbugs/jsr305]]]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"
                  :exclusions [args4j]]
                 [day8.re-frame/http-fx "0.1.6"]
                 [secretary "1.2.3"]
                 [antizer "0.3.1"
                  :exclusions [args4j]]
                 [cljsjs/react-leaflet "2.0.1-0"
                  :exclusions [[cljsjs/react]
                               [cljsjs/react-dom]]]]

  :clean-targets ^{:protect false} ["target"
                                    "node_modules"
                                    "package.json"
                                    "package-lock.json"
                                    "resources/public/js"
                                    "resources/public/css"
                                    "figwheel_server.log"]

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :compiler     {:main                 metr-fe.core
                    :closure-defines      {goog.DEBUG true}
                    :optimizations        :none
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/out"
                    :asset-path           "js/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}}
     :figwheel     {:websocket-url "ws://[[server-hostname]]:[[server-port]]/figwheel-ws"
                    :on-jsload     "metr-fe.core/mount-root"}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            metr-fe.core
                    :closure-defines {goog.DEBUG false}
                    :output-to       "resources/public/js/app.js"
                    :optimizations   :advanced
                    :pretty-print    false}}]}

  :less {:source-paths ["src/less" "node_modules"]
         :target-path  "resources/public/css"}

  :profiles
  {:dev {:plugins        [[lein-figwheel "0.5.17"]
                          [lein-doo "0.1.10"]]
         :dependencies   [[binaryage/devtools "0.9.10"]
                          [figwheel-sidecar "0.5.17"
                           :exclusions [[org.clojure/tools.nrepl]
                                        [args4j]]]
                          [cider/piggieback "0.3.10"
                           :exclusions [[org.clojure/tools.logging]
                                        [args4j]]]]
         :source-paths   ["src/clj/dev"]
         :resource-paths ["resources" "node_modules"]
         :repl-options   {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
         :less           {:source-map true}
         :figwheel       {:server-port 8081
                          :css-dirs    ["resources/public/css"]}}}

  :aliases
  {"dev"   ["with-profile" "+dev" "do"
            ["clean"]
            ["cljsbuild" "once" "dev"]
            ["less4j" "once"]
            ["figwheel" "dev"]]
   "build" ["with-profile" "-dev" "do"
            ["clean"]
            ["less4j" "once"]
            ["cljsbuild" "once" "min"]]})
