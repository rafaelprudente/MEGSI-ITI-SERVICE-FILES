#!/bin/bash

git pull

mvn clean package -DskipTests

arch=$(uname -m)
if [[ "$arch" == "x86_64" ]]; then
  docker buildx build --platform linux/amd64 -t iti-service-auth:latest .
elif [[ "$arch" == "aarch64" ]]; then
  docker buildx build --platform linux/arm64 -t iti-service-auth:latest .
else
  echo "Invalid platform: $arch"
fi
