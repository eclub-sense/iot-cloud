curl -X POST 127.0.0.1:8080/sensor_registration -H "Content-Type: application/json" -d '{"uuid" : 0, "type" : "THERMOMETER", "secret" : "00000000000000000000000000000000000000000000000000000000"}'

curl -X POST 127.0.0.1:8080/sensor_registration -H "Content-Type: application/json" -d '{"uuid" : 1, "type" : "THERMOMETER", "secret" : "00000000000000000000000000000000000000000000000000000000"}'

curl -X POST 127.0.0.1:8080/sensor_registration -H "Content-Type: application/json" -d '{"uuid" : 2, "type" : "THERMOMETER", "secret" : "00000000000000000000000000000000000000000000000000000000"}'

curl -X POST 127.0.0.1:8080/sensor_registration -H "Content-Type: application/json" -d '{"uuid" : 3, "type" : "LED", "secret" : "00000000000000000000000000000000000000000000000000000000"}'

curl -X POST 127.0.0.1:8080/sensor_registration -H "Content-Type: application/json" -d '{"uuid" : 4, "type" : "THERMOMETER", "secret" : "00000000000000000000000000000000000000000000000000000000"}'

curl -X POST 127.0.0.1:8080/sensor_registration -H "Content-Type: application/json" -d '{"uuid" : 5, "type" : "LED", "secret" : "00000000000000000000000000000000000000000000000000000000"}'
