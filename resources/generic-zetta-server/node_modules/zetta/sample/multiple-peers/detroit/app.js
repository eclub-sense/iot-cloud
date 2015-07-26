module.exports = function(server) {
  var query = server.ql('where type = "photocell"');
  var subscription = server
    .observe([query])
    .map(function(d) { return 'id is ' + d.id; })
    .subscribe(console.log, console.error);
};
