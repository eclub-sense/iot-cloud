curl -k -X POST https://zettor.sin.cvut.cz:8082/share_sensor?access_token="$2" -H "Content-Type: application/json" -d '{"uuid":"'$1'","access":"public"}'
