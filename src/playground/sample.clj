(ns playground.sample
  (:require [playground.core :refer [deflambdaring]]
            [ring.util.response :as r]
            [compojure.core :refer :all]))

(defn simple-ring-handler
  [request]
  (r/response "Hello Lambda, from ring!"))

(deflambdaring playground.sample.SimpleRingHandler simple-ring-handler)

;; TODO for some odd reason I can't setup 2 lambda handlers here?
;; TODO for some reason this compojure app returns null as the body?
;; (defroutes compojure-app
;;   (GET "/helloCompojure/:helloId" [helloId]
;;        (str "Hello compojure " helloId "!")))

;; (deflambdaring playground.sample.CompojureHandler compojure-app)
