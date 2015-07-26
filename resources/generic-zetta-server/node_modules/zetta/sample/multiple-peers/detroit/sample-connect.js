var http = require('http');
var url = require('url');
var crypto = require('crypto');


var parsed = url.parse(process.argv[2]);
var host = parsed.hostname;
var port = parsed.port;

// begin handshake
var key = new Buffer('13' + '-' + Date.now()).toString('base64');
var shasum = crypto.createHash('sha1');
shasum.update(key + '258EAFA5-E914-47DA-95CA-C5AB0DC85B11');
var expectedServerKey = shasum.digest('base64');

var opts = {
  host: host,
  port: port,
  path: '/peers/test',
  method: 'GET',
  headers: {
    'Connection': 'Upgrade',
    'Upgrade': 'websocket',
    'Host': parsed.host,
    'Sec-WebSocket-Version': '13',
    'Sec-WebSocket-Key': key
  }
};

var req = http.request(opts);
req.on('upgrade', function(res, socket) {
  console.log('upgrade');
  setTimeout(function() {
    socket.end();
  }, 30000)
});

req.on('error', console.error);
req.end();

