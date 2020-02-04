#!/bin/sh

../gradlew build
docker build -t haipiaodev/user-service:0.0.6 .