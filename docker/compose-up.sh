#!/bin/bash

docker-compose \
  -f services/network/network-compose.yml \
  -f services/consul/consul-compose.yml \
  -f services/db/db-compose.yml \
  -f services/keycloak/keycloak-compose.yml \
  -f services/microchat/sys-routing.yml \
  -f services/microchat/sys-keycloak.yml \
  up -d
