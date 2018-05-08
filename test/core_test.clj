(ns playground.core-test
  (:require [clojure.test :refer :all]
            [playground.core :refer :all]
            [clojure.java.io :as io]
            [clojure.data.json :as json])
  (:import [playground.core Handler]
           [java.io ByteArrayOutputStream]))

(deftest test-handle-event
  (testing "handle event creates a valid response."
    (is (every? (handle-event nil) [:statusCode :headers :body]))))

(deftest test-handle-request
  (let [handler (Handler.)
        in (io/input-stream (.getBytes "{}"))
        out (ByteArrayOutputStream.)
        ctx nil]
    (testing "writes to response to output stream."
      (.handleRequest handler in out ctx)
      (let [result (json/read-str (.toString out))]
        (is (every? #(get result %1) ["statusCode" "headers" "body"]))))))
