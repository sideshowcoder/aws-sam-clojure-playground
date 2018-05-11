(ns playground.core
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [ring.util.codec :as codec]
            [ring.util.response :as r])
  (:gen-class
   :name playground.core.Handler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:import [com.amazonaws.services.lambda.runtime RequestStreamHandler]
           [java.io InputStream File]
           [clojure.lang ISeq]))

(defn sample-handler
  [event]
  (r/response "Hello Lambda, from ring!"))

(defn stream->event
  [in]
  (json/read (io/reader in) :key-fn keyword))

(defn str->int
  [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException _)))

(defn maybe-remote-address
  "Take a guess at the remote client ip based on the X-FORWARDED-FOR header.
  This is not perfect as the X-FORWARDED-FOR can easily be spoofed. "
  [x-forwarded-for]
  (some-> x-forwarded-for
          (s/split #"," 2)
          first
          s/trim))

(defn maybe-http-version
  "Take http version frim VIA header if present"
  [via]
  (some-> via
          (s/split #" " 2)
          first
          s/trim))

(defn api-gw-event->ring-request
  ([ev] (api-gw-event->ring-request ev nil))
  ([ev ctx]
   (let [headers (get ev :headers {})]
     {:server-port (str->int (:X-Forwarded-Port headers))
      :body (:body ev)
      :server-name (:Host headers)
      :remote-addr (maybe-remote-address (:X-Forwarded-For headers))
      :uri (:path ev)
      :query-string (codec/form-encode (get ev :queryStringParameters {}))
      :scheme (maybe-http-version (:Via headers))
      :request-method (:httpMethod ev)
      :protocol (:X-Forwarded-Proto headers)
      :headers headers
      :event ev
      :context ctx})))

(defmulti wrap-body class)
(defmethod wrap-body String [body] body)
(defmethod wrap-body ISeq [body] (s/join body))
(defmethod wrap-body File [body] (slurp body))
(defmethod wrap-body InputStream [body] (slurp body))

(defn ring-response->api-gw-response
  [response]
  {:statuCode (:status response)
   :headers (:headers response)
   :body (wrap-body (:body response))})

(defn handle-request
  "Handle an api-gateway request from IN via a RING-HANDLER writing
  response to OUT."
  [ring-handler in out ctx]
  (let [event (stream->event in)
        request (api-gw-event->ring-request event ctx)
        response (ring-handler request)
        event-response (ring-response->api-gw-response response)]
    (with-open [w (io/writer out)]
      (json/write event-response w))))

(defn -handleRequest
  "Implementation for RequestStreamHandler handleRequest using IN
  instream OUT outstream and CTX context object, delegating to
  handle-request to handle request using ring"
  [_ in out ctx]
  (handle-request sample-handler in out ctx))
