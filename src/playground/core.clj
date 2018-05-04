(ns playground.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:gen-class
   :name playground.core.Handler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:import [com.amazonaws.services.lambda.runtime RequestStreamHandler]))

(defn handle-event
  [event]
  (println event)
  {:statusCode 200 :headers {} :body "Hello Lambda"})

(defn -handleRequest
  [_ in out ctx]
  (let [ev (json/read (io/reader in))
        res (handle-event ev)]
    (with-open [w (io/writer out)]
      (json/write res w))))

;; Testing locally
;; (let [in (io/input-stream (.getBytes "foo"))
;;       out (ByteArrayOutputStream.)]
;;   (.handleRequest (playground.core.Handler.) in out nil)
;;   (.toString out))
