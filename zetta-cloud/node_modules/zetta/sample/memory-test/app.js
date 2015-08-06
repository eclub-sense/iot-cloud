module.exports = function(server) {
  var servers = []; // keep a list of peers subscribed to, to filter duplicates
  server.pubsub.subscribe('_peer/connect', function(e, msg) {
    if (servers.indexOf(msg.peer.name) > -1) {
      return;
    }
    servers.push(msg.peer.name); // _peer/connect is called on reconnect, must filter

    var query = server.from(msg.peer.name).ql('where type is not missing');
    server.observe(query, function(device) {  });
  });
};
