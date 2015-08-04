curl -u "$2":"$3" -X POST 127.0.0.1:8080/hub_registration -H "Content-Type: application/json" -d '{\"type\":\"LOGIN\",\"username\":\"user\",\"password\":\"123\","uuid":'$1'}'
