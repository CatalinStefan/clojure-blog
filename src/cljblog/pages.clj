(ns cljblog.pages
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.form :as form]
   [markdown.core :as md]

   [ring.util.anti-forgery :refer [anti-forgery-field]]
   ))


(defn base-page [& body]
  (html5
   [:head [:title "CljBlog"]
    [:link {:rel "stylesheet"
            :href "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
            :integrity "sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
            :crossorigin "anonymous"}]]
   [:body
    [:div.container
     [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
      [:a.navabar-brand {:href "/"}  "CljBlog"]
      [:div.navbar-nav.ml-auto
       [:a.nav-item.nav-link {:href "/articles/new"} "New article!"]
       [:a.nav-item.nav-link {:href "/admin/login"} "LogIn"]
       [:a.nav-item.nav-link {:href "/admin/logout"} "LogOut"]

       ]]

     body]]))


(def preview-len 270)

(defn- cut-body [body]
  (if (> (.length body) preview-len)
    (subs body 0 preview-len)
    body))


(defn index [articles]
  (base-page
   (for [a articles]
     [:div
      [:h2 [:a {:href (str "/articles/" (:_id a))}(:title a)]]
      [:p (-> a :body cut-body md/md-to-html-string)]
      ]
     )))


(defn article [a]
  (base-page
   (form/form-to
    [:delete (str "/articles/" (:_id a))]
    (anti-forgery-field)
    [:a.btn.btn-primary {:href (str "/articles/" (:_id a) "/edit")} "edit"]
    (form/submit-button {:class "btn btn-danger"} "Delete"))
   [:small (:created a)]
   [:h1 (:title a)]
   [:p (-> a :body md/md-to-html-string)]))


(defn edit-article [a]
  (base-page
   (form/form-to
    [:post (if a
             (str "/articles/" (:_id a))
             "/articles")]

    [:div.form-group
     (form/label "title" "Title")
     (form/text-field {:class "form-control"} "title" (:title a))]

    [:div.form-group
     (form/label "body" "Body")
     (form/text-area {:class "form-control"} "body" (:body a))]


    (anti-forgery-field)

    (form/submit-button {:class "btn btn-primary"} "Save!")

    )))


(defn admin-login [& [msg]]
  (base-page
   (when msg
     [:div.alert.alert-danger msg])
   (form/form-to
    [:post "/admin/login"]

    [:div.form-group
     (form/label "login" "Login")
     (form/text-field {:class "form-control"} "login")]

    [:div.form-group
     (form/label "password" "Password")
     (form/password-field {:class "form-control"} "password")]

    (anti-forgery-field)
    (form/submit-button {:class "btn btn-primary"} "Login!")

    )))
