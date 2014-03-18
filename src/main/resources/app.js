/**
 * Created by spousty on 3/15/14.
 */

var vertx = require('vertx');
var container = require('vertx/container');
var console = require('vertx/console');

var ip = container.env['OPENSHIFT_VERTX_IP'] || '127.0.0.1';
var port = parseInt(container.env['OPENSHIFT_VERTX_PORT'] || 8080);


//may want to add
// gzip_file: true
//
// By default the SockJS bridge registers at URL prefix:
// {"prefix": "/eventbus"}
container.deployModule("io.vertx~mod-web-server~2.0.0-final",
    {   port: port,
        host: ip,
        web_root: "web",
        index_page: "index.html",
        bridge: true,
        outbound_permitted: [
            { address: "flights.updated" }
        ]
    }, function(err, deployID){
    console.log("started web server")});

container.deployVerticle("com.openshift.FeedGetter");
container.deployVerticle("FlightPublisher.py")
