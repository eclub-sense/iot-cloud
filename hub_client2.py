# Sends messages to cloud server and prints acknowledgements.

from websocket import create_connection
import time
ws = create_connection("ws://127.0.0.1:8080/events/")
#result = ws.recv()
#print "Received '%s'" % result
ws.send("Hello Server2")
#result = ws.recv()
#print "Received '%s'" % result
for i in range (1, 10):
	ws.send("Hello Server2")
	time.sleep(1)
#result = ws.recv()
#print "Received '%s'" % result
ws.close()
