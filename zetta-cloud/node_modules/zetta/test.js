var assert = require('assert');
var LED = require('./test/fixture/example_driver');
var zetta = require('./zetta');

var d = require('domain').create();
d.on('error', function(err) {
  console.log('error:', err)
});
d.run(function() {
  zetta()
    .use(LED)
    .use(function(server) {
      var ledQuery = server.where({ type: 'testdriver' });
      server.observe(ledQuery, function(led) {
        console.log('Should Throwz');
        throw new Error('123');
      })
    })
    .listen(0);
});
