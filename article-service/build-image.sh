#!/bin/sh

../gradlew build
docker build -t haipiaodev/article-service:0.0.1 .