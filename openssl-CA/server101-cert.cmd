openssl x509 -req -in val/server101.csr -CA val/rootCA.crt -CAkey val/rootCA.key -CAcreateserial -out val/server101.crt -days 5000