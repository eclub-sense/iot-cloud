var uuid = require('node-uuid');
module.exports = function(server) {
  var ledQueryDet = server.from('*').where({ type: 'led' });
  server.observe([ledQueryDet], function(det) {
    console.log('found led', det.id)
  });
};
