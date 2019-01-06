set httpaddr=http://192.168.56.101:8500
consul kv put -http-addr=%httpaddr% config/sys-routing/data @sys-routing.yml
consul kv put -http-addr=%httpaddr% config/app-props/data @app-props.yml
