(ns playground.sampleapp
  (:use compojure.core)
  (:require [playground.core :refer [deflambdaring]]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [compojure.handler :as handler]))

(defroutes sample-app
  (GET "/sayHello/:hello" [hello]
       (let [name (if (nil? hello) "Lambda" hello)]
         (resp/response (str "Hello " name ", from compojure!")))))


(def ring-app
  (-> (handler/api sample-app)))

(deflambdaring playground.sampleapp.Handler
  [in out ctx]
  ())
