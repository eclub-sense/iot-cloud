from websocket import create_connection
import time
ws = create_connection("ws://127.0.0.1:8080/events/")
ws.send("{\"type\":\"LOGIN\",\"uuid\":111,\"username\":\"User\",\"password\":\"123\"}")
time.sleep(7)
#ws.send("{\"type\":\"DATA\",\"uuid\":\"00000001\",\"data\":\"0041FF00000000000001000000020000000000000000000000000000\"}")
ws.close()
