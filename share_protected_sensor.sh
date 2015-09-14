curl -k -X POST https://147.32.107.139:8082/share_sensor?access_token="$4" -H "Content-Type: application/json" -d '{"uuid":"'$1'","access":"protected","email":"'$2'","permission":"'$3'"}'
