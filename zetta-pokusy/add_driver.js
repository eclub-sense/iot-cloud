module.exports = function(server) {
  var senderQuery = server
        .from('*')
        .where({ type: 'sender' });
  var registratorQuery = server
        .from('*')
        .where({ type: 'registrator' });
  var uuid = 0;

  server.observe([registratorQuery, senderQuery], function(registrator, sender) {
	console.log('Found sender: '+ sender.id);
	console.log('Found registrator: '+registrator.id);

    registrator.streams.uuid.on('data', function(m) {
      if(m.data != uuid) { //register to all hubs!!!
		console.log('CLOUD: register: ' + registrator.uuid);
		uuid = m.data;
        sender.call('send', registrator.uuid, registrator.sensor_type, function(err) { });
      }
    });
  });
};
