(ns addrbook.routes
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer :all])
  (:use [addrbook.handler]))

(defroutes app-routes
  (GET "/" [] check-signature)
  (POST "/" [] service)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
