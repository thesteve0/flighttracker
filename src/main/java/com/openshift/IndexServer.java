package com.openshift;

/**
 * @TODO this file will be used to serve up the base html page
 *
 * Created by spousty on 3/11/14.
 */
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;

import java.util.Map;

public class IndexServer extends Verticle {

    public void start() {
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response().headers().set("Content-Type", "text/plain");
                req.response().end("Hello World");
            }
        }).listen(8085);
    }
}
