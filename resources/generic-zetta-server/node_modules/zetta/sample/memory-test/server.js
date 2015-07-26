var zetta = require('../../');
var app = require('./app');
var heapdump = require('heapdump');

zetta()
  .use(app)
  .silent()
  .listen(3000);
