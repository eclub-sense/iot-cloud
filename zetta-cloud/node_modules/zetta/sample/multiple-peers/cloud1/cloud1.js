var zetta = require('../../../');
var app = require('./app');
var statsapp = require('./statsapp')
var LED = require('zetta-led-mock-driver');
var UsageApp = require('zetta-usage-addon');

//var app = new UsageApp()

zetta()
  .use(app)
  .listen(2000);

