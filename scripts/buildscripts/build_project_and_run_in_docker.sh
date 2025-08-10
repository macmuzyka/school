#!/bin/bash
set -e

MODE=${1:-tests}  # default to "tests" if no arg

if [[ "$MODE" == "notests" ]]; then
  MVN_CMD="mvn clean install -DskipTests"
else
  MVN_CMD="mvn clean install"
fi

# Step 1: Maven build
echo -e "\e[36m📦 Setting up auxiliary Kafka KRaft container..\e[0m"
docker run -d --name kafka-kraft -p 9092:9092 bashj79/kafka-kraft:latest

echo "🔨 Running Maven build: $MVN_CMD"
if $MVN_CMD; then
  echo -e "\e[32m✅ Maven build succeeded.\e[0m"
else
  echo -e "\e[31m❌ Maven build failed. Checking reports...\e[0m"
  if [[ -d target/surefire-reports ]]; then
    egrep -H "FAILURE|AssertionFailedError" target/surefire-reports/*.txt || true
  fi
  echo -e "\e[31m While in failure stopping auxiliary kafka-kraft container\e[0m"
  docker stop kafka-kraft
  echo -e "\e[31m While in failure removing auxiliary kafka-kraft container\e[0m"
  docker container rm kafka-kraft
  exit 1
fi
echo -e "\e[36m Stopping auxiliary kafka-kraft container after successful maven build..\e[0m"
docker stop kafka-kraft
echo -e "\e[36m Removing auxiliary kafka-kraft container after successful maven build..\e[0m"
docker container rm kafka-kraft

# Step 2: Docker build
echo -e "\e[36m🐳 Building school project Docker image...\e[0m"
if docker build -t school .; then
  echo -e "\e[32m✅ Docker image built successfully.\e[0m"
else
  echo -e "\e[31m❌ Docker build failed.\e[0m"
  exit 1
fi

# Step 3: Docker Compose up
echo -e "\e[36m🚀 Starting containers...\e[0m"
if docker-compose up -d; then
  echo -e "\e[32m✅ Containers started successfully.\e[0m"
else
  echo -e "\e[31m❌ docker-compose up failed.\e[0m"
  exit 1
fi
