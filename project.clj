(defproject cljblog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [com.novemberain/monger "3.1.0"]
                 [markdown-clj "1.10.5"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler cljblog.handler/app}
  :main cljblog.web
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
