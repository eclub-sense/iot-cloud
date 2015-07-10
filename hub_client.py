# Sends messages to cloud server and prints acknowledgements.

from websocket import create_connection
ws = create_connection("ws://127.0.0.1:8080/peers/zetta-server")
#result = ws.recv()
#print "Received '%s'" % result
ws.send("Hello Server")
#result = ws.recv()
#print "Received '%s'" % result
for i in range (1, 1000):
	ws.send_binary("Hello Server")
#result = ws.recv()
#print "Received '%s'" % result
ws.close()
