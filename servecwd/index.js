var express = require('express');
var serveIndex = require('serve-index');
var app = express();

express.static.mime.define({'text/plain': ['erb']});

app.use(express.static(process.env.PWD));
app.use(serveIndex(process.env.PWD, {icons : true}));
app.use(function(err, req, res, next) {
  console.error(err.stack);
  res.status(500).send('Something broke!');
});

var server = app.listen(3000, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);
});
