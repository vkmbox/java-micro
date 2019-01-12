#!/bin/bash

docker-compose \
  -f services/consul/consul-compose.yml \
  -f services/db/db-compose.yml \
  -f services/keycloak/keycloak-compose.yml \
  -f services/microchat/microchat-compose.yml \
  stop
