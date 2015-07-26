var cluster = require('zetta-cluster');
var SineWave = require('zetta-sine-wave');
var LED = require('zetta-mock-led');

cluster()
  .server('cloud1')
  .server('cloud2')
  .server('Detroit', [SineWave, LED], ['cloud1', 'cloud2'])
  .on('ready', function(){
    console.log('all ready')
  })
  .run(function() {
    console.log('running')
  });

