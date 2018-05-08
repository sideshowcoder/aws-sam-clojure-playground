(ns playground.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:gen-class
   :name playground.core.Handler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:import [com.amazonaws.services.lambda.runtime RequestStreamHandler]))

(defn handle-event
  [event]
  {:statusCode 200 :headers {} :body "Hello Lambda"})

(defn event->request
  [ev ctx]
  {:server-port nil
   :body nil
   :server-name nil
   :remote-addr nil
   :uri nil
   :query-string nil
   :scheme nil
   :request-method nil
   :protocol nil
   :headers nil
   :event ev
   :context ctx})

(defn response->api-gw-response
  [r]
  {:statusCode (:status r)
   :body (json/write-json (:body r))
   :headers (:headers r)})

(defn -handleRequest
  [_ in out ctx]
  (let [ev (json/read (io/reader in))
        res (handle-event ev)]
    (with-open [w (io/writer out)]
      (json/write res w))))



;; TODO integrate this with ring, aka make handle-event a ring handler
;; TODO create a ring adapter around api-gateway
