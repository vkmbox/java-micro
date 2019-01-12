#!/bin/bash

mvn clean package
docker build -t microchat/sys-routing:latest .
