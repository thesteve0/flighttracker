package com.openshift;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * Created by spousty on 3/11/14.
 */
public class FeedGetter extends Verticle{

    @Override
    public void start(){

        //Make out Pool of HTTP client connections
        //can not use keep
        final HttpClient webClient = vertx.createHttpClient().setSSL(true).setTrustAll(true).setPort(443).setKeepAlive(true).setHost("api.flightstats.com");
        final String appid = System.getenv("OPENSHIFT_FLIGHTSTATS_ID");
        final String appkey = System.getenv("OPENSHIFT_FLIGHTSTATS_KEY");


        final String url = "/flex/flightstatus/rest/v2/json/fleet/tracks/UA?appId="+appid+"&appKey="+appkey+"&includeFlightPlan=false&maxPositions=1&codeshares=false&maxFlights=300";
        System.out.println("URL = " + url);
        //Ok start by setting a job that grabs the feed every 10 seconds
        vertx.setPeriodic(10000, new Handler<Long>() {


            public void handle(Long timerID){

                webClient.getNow(url, new Handler<HttpClientResponse>() {
                    @Override
                    public void handle(HttpClientResponse httpClientResponse) {
                        httpClientResponse.bodyHandler(new Handler<Buffer>(){
                            public void handle(Buffer data){
                                JsonObject allJSON = new JsonObject(data.toString());
                                JsonArray flightsArray = allJSON.getField("flightTracks");

                                //We are now set to put the flightsArray on the bus - then the python verticle will grab the Array and Process it.
                                vertx.eventBus().publish("raw.flights", flightsArray);

                                System.out.println("grabbed the raw flights: " + flightsArray.size());
                            }
                        });
                    }
                });

            }

        }

        );

    }




}
