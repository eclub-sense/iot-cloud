var util = require('util');
var Device = require('zetta-device');

var DriverSender = module.exports = function(sender) {
  this.sender = sender;
  this.payload = 0
  Device.call(this);
};
util.inherits(DriverSender, Device);

DriverSender.prototype.init = function(config) {
  config
    .type('sender')
    .state('ready')
    .name(this.sender)
    .when('ready', { allow: ['send'] })
    .when('send', { allow: ['ready'] })
    .map('send', this.send, [{type:'number', name:'uuid'}, {type:'number', name:'type'}])
    .map('ready', this.ready)
    .monitor('payload');
};

DriverSender.prototype.send = function(uuid, type, cb) {
  var self = this;
console.log('LED: Registering: uuid:'+uuid+' type: '+ type);
  this.state = 'send';
  self.payload = self.payload + 1;
  this.state = 'ready';
  cb();
};
