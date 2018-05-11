(ns playground.core-test
  (:require [clojure.test :refer :all]
            [playground.core :refer :all]
            [clojure.java.io :as io]
            [clojure.data.json :as json])
  (:import [java.io ByteArrayOutputStream]))

(def sam-local-sample-event "{
  \"resource\": \"/sayHello/world\",
  \"body\": \"\",
  \"requestContext\": {
    \"resourcePath\": \"/sayHello/world\",
    \"httpMethod\": \"GET\",
    \"stage\": \"prod\",
    \"identity\": {
      \"sourceIp\": \"127.0.0.1:54403\"
    }
  },
  \"path\": \"/sayHello/world\",
  \"httpMethod\": \"GET\",
  \"pathParameters\": {
    \"helloId\": \"world\"
  },
  \"queryStringParameters\": {
    \"foo\": \"1\",
    \"some\": \"queryparam\"
  },
  \"stageVariables\": null,
  \"isBase64Encoded\": false,
  \"headers\": {
    \"X-Forwarded-Port\": \"\",
    \"Content-Length\": \"0\",
    \"Connection\": \"keep-alive\",
    \"Mime-Version\": \"1.0\",
    \"Extension\": \"Security/Digest Security/SSL\",
    \"Accept-Charset\": \"utf-8;q=1\",
    \"X-Forwarded-Proto\": \"\",
    \"Accept\": \"*/*\",
    \"Host\": \"127.0.0.1\"
  }
}")

(def api-gateway-proxy-request-event "{
  \"path\": \"/test/hello\",
  \"headers\": {
    \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\",
    \"Accept-Encoding\": \"gzip, deflate, lzma, sdch, br\",
    \"Accept-Language\": \"en-US,en;q=0.8\",
    \"CloudFront-Forwarded-Proto\": \"https\",
    \"CloudFront-Is-Desktop-Viewer\": \"true\",
    \"CloudFront-Is-Mobile-Viewer\": \"false\",
    \"CloudFront-Is-SmartTV-Viewer\": \"false\",
    \"CloudFront-Is-Tablet-Viewer\": \"false\",
    \"CloudFront-Viewer-Country\": \"US\",
    \"Host\": \"wt6mne2s9k.execute-api.us-west-2.amazonaws.com\",
    \"Upgrade-Insecure-Requests\": \"1\",
    \"User-Agent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36 OPR/39.0.2256.48\",
    \"Via\": \"1.1 fb7cca60f0ecd82ce07790c9c5eef16c.cloudfront.net (CloudFront)\",
    \"X-Amz-Cf-Id\": \"nBsWBOrSHMgnaROZJK1wGCZ9PcRcSpq_oSXZNQwQ10OTZL4cimZo3g==\",
    \"X-Forwarded-For\": \"192.168.100.1, 192.168.1.1\",
    \"X-Forwarded-Port\": \"443\",
    \"X-Forwarded-Proto\": \"https\"
  },
  \"pathParameters\": {
    \"proxy\": \"hello\"
  },
  \"requestContext\": {
    \"accountId\": \"123456789012\",
    \"resourceId\": \"us4z18\",
    \"stage\": \"test\",
    \"requestId\": \"41b45ea3-70b5-11e6-b7bd-69b5aaebc7d9\",
    \"identity\": {
      \"cognitoIdentityPoolId\": \"\",
      \"accountId\": \"\",
      \"cognitoIdentityId\": \"\",
      \"caller\": \"\",
      \"apiKey\": \"\",
      \"sourceIp\": \"192.168.100.1\",
      \"cognitoAuthenticationType\": \"\",
      \"cognitoAuthenticationProvider\": \"\",
      \"userArn\": \"\",
      \"userAgent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36 OPR/39.0.2256.48\",
      \"user\": \"\"
    },
    \"resourcePath\": \"/{proxy+}\",
    \"httpMethod\": \"GET\",
    \"apiId\": \"wt6mne2s9k\"
  },
  \"resource\": \"/{proxy+}\",
  \"httpMethod\": \"GET\",
  \"queryStringParameters\": {
    \"name\": \"me\"
  },
  \"stageVariables\": {
    \"stageVarName\": \"stageVarValue\"
  }
}")

(deftest test-stream->event
  (testing "transform input stream to event map"
    (let [stream (io/input-stream (.getBytes aws-gw-sample-event))]
      (is (every? (stream->event stream) [:headers :body])))))

