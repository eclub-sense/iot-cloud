# Sends messages to cloud server and prints acknowledgements.

from websocket import create_connection
ws = create_connection("ws://127.0.0.1:8080")
result = ws.recv()
print "Received '%s'" % result
ws.send("Hello Server")
result = ws.recv()
print "Received '%s'" % result
ws.send_binary("Hello Server")
result = ws.recv()
print "Received '%s'" % result
ws.close()
