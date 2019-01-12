#!/bin/bash

mvn clean package
docker build -t chat-micro/sys-routing:latest
