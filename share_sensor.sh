curl -u DAT:567 -X POST 127.0.0.1:8080/share_sensor -H "Content-Type: application/json" -d '{"username":"'$1'","uuid":"'$2'","permission":"'$3'"}'
