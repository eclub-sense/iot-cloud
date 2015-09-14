curl -k -X DELETE https://147.32.107.139:8082/delete_sensor?access_token="$2" -H "Content-Type: application/json" -d '{"uuid":"'$1'"}'
