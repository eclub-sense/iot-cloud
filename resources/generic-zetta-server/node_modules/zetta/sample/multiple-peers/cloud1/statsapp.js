module.exports = function(server) {
  server.pubsub.subscribe('_peer/connect', function(event, info) {
    info.peer.ws.on('data', function(data) {
    });
    console.log(info.peer.ws)
  });
};
