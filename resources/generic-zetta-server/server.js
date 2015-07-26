var zetta = require('zetta');

zetta()
  .name('>>SERVER_NAME<<')
  .expose('*')
  .listen(>>PORT<<, function(err) {
    if(err) {
      console.error(err);
      process.exit(1);
    }
    console.log('running on http://localhost:', >>PORT<<)
  });
