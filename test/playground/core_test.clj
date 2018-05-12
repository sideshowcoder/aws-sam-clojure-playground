(ns playground.core-test
  (:require [clojure.test :refer :all]
            [playground.core :refer :all]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [ring.util.response :as r])
  (:import [java.io ByteArrayOutputStream]))

(deftest test-stream->event
  (testing "transform input stream to event map"
    (let [stream (io/input-stream (io/resource "test/sam_local_event.json"))]
      (is (every? (stream->event stream) [:headers :body])))))

(deftest test-ring-response->api-gw-response
  (testing "transform a ring response to api gateway response"
    (let [body "test-body"]
      (is (= (ring-response->api-gw-response (r/response body)) {:statusCode 200 :headers {} :body body})))))

(defn test-handler
  [request]
  (r/response "test-body"))

(deftest test-handle-request
  (let [in (io/input-stream (io/resource "test/sam_local_event.json"))
        out (ByteArrayOutputStream.)
        context nil]
    (handle-request test-handler in out context)
    (let [result (json/read-str (str out) :key-fn keyword)]
      (is (= {:statusCode 200, :headers {}, :body "test-body"} result)))))

(deftest test-wrap-body-supports-ring-body-types
  (testing "support String"
    (is (= (wrap-body "foo") "foo")))
  (testing "support Sequence"
    (is (= (wrap-body (seq [1 2 3])) "123")))
  (testing "support File"
    (is (= (wrap-body (io/as-file (io/resource "test/test.txt"))) "test\n")))
  (testing "support InputStream"
    (is (= (wrap-body (io/input-stream (io/resource "test/test.txt"))) "test\n"))))
