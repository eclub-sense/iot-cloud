var path = require('path');
var url = require('url');

module.exports = function(handle) {
  handle('request', function(env, next) {
    var uri = parseUri(env);

    env.helpers = env.helpers || {};
    env.helpers.url = {};

    env.helpers.url.join = function(pathname) {
      var parsed = url.parse(uri);
      parsed.search = null;
      parsed.pathname = path.join(parsed.pathname, pathname).replace(/\\/g, '/');
      
      return url.format(parsed);
    };

    env.helpers.url.path = function(pathname) {
      var parsed = url.parse(uri);
      parsed.search = null;
      parsed.pathname = pathname;

      return url.format(parsed);
    };

    env.helpers.url.current = function() {
      return uri;
    };

    next(env);
  });
};

function parseUri(env) {
  var xfp = env.request.headers['x-forwarded-proto'];
  var xfh = env.request.headers['x-forwarded-host'];
  var protocol;

  if (xfp && xfp.length) {
    protocol = xfp.replace(/\s*/, '').split(',')[0];
  } else {
    protocol = env.request.connection.encrypted ? 'https' : 'http';
  }

  var host = xfh || env.request.headers['host'];

  if (!host) {
    var address = env.request.connection.address();
    host = address.address;
    if (address.port) {
      if (!(protocol === 'https' && address.port === 443) && 
          !(protocol === 'http' && address.port === 80)) {
        host += ':' + address.port
      }
    }
  }

  return protocol + '://' + path.join(host, env.request.url);
}
