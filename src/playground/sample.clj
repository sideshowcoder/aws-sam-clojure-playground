(ns playground.sample
  (:require [playground.core :refer :all]
            [ring.util.response :as r]
            [compojure.core :refer :all]))

(defn simple-ring-handler
  [request]
  (r/response "Hello Lambda, from ring!"))

(deflambdaring playground.sample.SimpleRingHandler simple-ring-handler)

(defroutes compojure-app
  (GET "/helloCompojure/:helloId" [helloId]
       (str "Hello compojure " helloId "!")))

(deflambdaring playground.sample.CompojureHandler compojure-app)
