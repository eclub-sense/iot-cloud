var util = require('util');
var Device = require('zetta-device');

var SensorRegistrator = module.exports = function(registrator) {
  this.registrator = registrator;
  this.uuid = 0;
  this.sensor_type = 0;
  Device.call(this);
};
util.inherits(SensorRegistrator, Device);

SensorRegistrator.prototype.init = function(config) {
  config
    .type('registrator')
    .state('ready')
    .name(this.registrator)
    .when('add_sensor', { allow: ['ready'] })
	.when('ready', { allow: ['add_sensor'] })
    .map('add_sensor', this.add_sensor)
	.map('ready', this.ready)
    .monitor('uuid')
	.monitor('sensor_type');
};

SensorRegistrator.prototype.add_sensor = function(cb) {
  var self = this;
  this.state = 'add_sensor';
  self.sensor_type = 0x41;
  self.uuid = self.uuid + 1;
  console.log('MOBILE: wants register uuid: '+self.uuid+', type: '+self.sensor_type);
  this.state = 'ready';
  cb();
};
