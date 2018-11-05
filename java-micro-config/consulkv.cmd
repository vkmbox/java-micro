set httpaddr=http://localhost:8500
consul kv put -http-addr=%httpaddr% config/sys-routing/data @sys-routing.yml