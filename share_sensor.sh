curl -u User:123 -X POST 127.0.0.1:8080/share_sensor -H "Content-Type: application/json" -d '{"uuid":"'$1'","access":"'$2'"}'
