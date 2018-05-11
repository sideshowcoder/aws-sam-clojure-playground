# AWS SAM Lambda clojure playground

Playing with AWS Lambda using SAM

## Usage

Build using `lein uberjar`
Run using `sam local start-api`
Test query see local-request.http

## Notes

Timeout set to 15s as it can take quite a while for clojure to boot and respond,
more than 3s anyway.

This is based on [ring-aws-lambda-adapter](https://github.com/jpb/ring-aws-lambda-adapter)
