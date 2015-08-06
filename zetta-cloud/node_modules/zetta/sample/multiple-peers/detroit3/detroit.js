var zetta = require('../../../');
var LED = require('zetta-led-mock-driver');
var Photocell = require('zetta-photocell-mock-driver');

zetta()
  .name('milford-123')
  .link('http://localhost:2000')
//  .link('http://core-03')
//  .link('http://core-serv-ZettaELB-1F8DYPOLJHJTW-1240763555.us-east-1.elb.amazonaws.com')
  .use(LED)
  .use(LED)
  .listen(0, function(){
    console.log('Zetta is running at http://127.0.0.1:1337');
  });
