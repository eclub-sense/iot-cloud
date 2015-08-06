var zetta = require('../../../');
var LED = require('zetta-led-mock-driver');
var Photocell = require('zetta-photocell-mock-driver');
var heapdump = require('heapdump');
var app = require('./app');

zetta()
  .name('milford1')
  .use(app)
  .use(LED, 1)
  .use(Photocell)
//  .use(Photocell)
  .use(LED, 2)
//  .use(app)
/*
  .use(function(server) {

    var addHeaders = function(req) {
      req.use(function(handle) {
        handle('request', function(pipeline) {
          return pipeline.map(function(env) {
            env.request.headers['X-Apigee-IoT-Tenant-ID'] = 'centralite:sandbox-user';              
            return env;
          });
        });
      });
    };

    server.onPeerRequest(addHeaders);
  })
*/
//  .link('http://testing2-ZettaELB-1T484YNBGTB8S-1389658208.us-east-1.elb.amazonaws.com')
//  .link('http://localhost:3000')
  .link('http://localhost:2000')
//  .link('http://testing2.iot.apigee.net')
//  .link('http://hello-zetta.herokuapp.com')
//  .link('http://testing.iot.apigee.net')
//  .link('http://centralit-ZettaELB-1PJYDRCS7P4AS-1329272028.us-east-1.elb.amazonaws.com')
//  .link('http://centralite.iot.apigee.net')
  .listen(1337, function(){
     console.log('Zetta is running at http://127.0.0.1:1337');
});
