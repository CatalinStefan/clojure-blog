(ns cljblog.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as resp]
            [ring.middleware.session :as session]

            [cljblog.db :as db]
            [cljblog.pages :as p]
            [cljblog.admin :as a]))

(defroutes app-routes
  (GET "/" [] (p/index (db/list-articles)))

  (GET "/articles/:art-id" [art-id] (p/article (db/get-article-by-id art-id)))

  (GET "/admin/login" [:as {session :session}]
       (if (:admin session)
         (resp/redirect "/")
         (p/admin-login)))

  (POST "/admin/login" [login password]
        (if (a/check-login login password)
          (-> (resp/redirect "/")
              (assoc-in [:session :admin] true))
          (p/admin-login "Invalid username or password!")))

  (GET "/admin/logout" []
       (-> (resp/redirect "/")
           (assoc-in [:session :admin] false)))


  (route/not-found "Not Found"))

(defroutes admin-routes
  (GET "/articles/new" [] (p/edit-article nil))

  (POST "/articles" [title body]
        (do (db/create-article title body)
            (resp/redirect "/")))

  (DELETE "/articles/:art-id" [art-id]
          (do (db/delete-article art-id)
              (resp/redirect "/")))

  (GET "/articles/:art-id/edit" [art-id] (p/edit-article (db/get-article-by-id art-id)))

  (POST "/articles/:art-id" [art-id title body]
        (do (db/update-article art-id title body)
            (resp/redirect (str "/articles/" art-id)))))


(defn wrap-admin-only [handler]
  (fn [request]
    (if (-> request :session :admin)
      (handler request)
      (resp/redirect "/admin/login"))))



(def app
  (-> (routes (wrap-routes admin-routes wrap-admin-only)
              app-routes)
      (wrap-defaults site-defaults)
      session/wrap-session))
