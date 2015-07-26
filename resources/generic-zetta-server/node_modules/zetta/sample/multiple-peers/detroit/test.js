var zetta = require('../../../');
var LED = require('zetta-led-mock-driver');
var Photocell = require('zetta-photocell-mock-driver');

zetta()
  .name('milford1')
  .use(function(server) {
    var addHeaders = function(req) {
      req.use(function(handle) {
        handle('request', function(pipeline) {
          return pipeline.map(function(env) {
            env.request.headers['X-Apigee-IoT-Tenant-ID'] = 'centralite:nml';              
            return env;
          });
        });
      });
    };
    server.onPeerRequest(addHeaders);
  })
  .link('http://centralite.iot.apigee.net')
  .listen(1337, function(){
    console.log('Zetta is running at http://127.0.0.1:1337');
  });
