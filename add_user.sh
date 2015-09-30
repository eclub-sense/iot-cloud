curl -X POST http://zettor.sin.cvut.cz:8080/user_registration -H "Content-Type: application/json" -d '{"grant_type":"authorization_code","client_id":"abeceda","code":"'$1'"}'
