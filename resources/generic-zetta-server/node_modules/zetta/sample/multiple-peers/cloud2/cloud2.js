var zetta = require('../../../');
var photocell = require('zetta-photocell-mock-driver');
var LED = require('zetta-mock-led')
zetta()
  .name('chicago')
//  .link('http://localhost:5000')
  .use(photocell)
  .use(LED)
  .listen(5001);

