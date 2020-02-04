#!/bin/sh

../gradlew build
docker build -t haipiaodev/registration:0.0.3 .