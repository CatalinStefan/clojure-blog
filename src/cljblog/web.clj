(ns cljblog.web
  (:require
   [ring.adapter.jetty :as jetty]
   [compojure.handler :as ch]

   [cljblog.handler :as blog])
  (:gen-class))


(defn -main [& args]
  (let [port (Integer. (or (System/getenv "CLJBLOG_PORT")
                           3000))]
    (jetty/run-jetty (ch/site #'blog/app)
                     {:port port
                      :join? false})))
