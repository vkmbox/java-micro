create user keycloak with encrypted password 'keycloak';
grant all privileges on database micro to keycloak;
create schema keycloak authorization keycloak;
