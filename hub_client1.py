# Sends messages to cloud server and prints acknowledgements.

from websocket import create_connection
import time
ws = create_connection("ws://127.0.0.1:8080/events/1")
#result = ws.recv()
#print "Received '%s'" % result
ws.send("Hello Server1")
#result = ws.recv()
#print "Received '%s'" % result
for i in range (1, 4):
	ws.send("Hello Server1")
	time.sleep(2)
#result = ws.recv()
#print "Received '%s'" % result
ws.close()
