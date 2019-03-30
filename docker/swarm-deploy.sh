#!/bin/bash

docker stack deploy \
  -c services/consul/consul-compose.yml \
  microchat
