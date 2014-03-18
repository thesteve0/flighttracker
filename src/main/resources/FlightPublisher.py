__author__ = 'spousty'

import vertx
from core.event_bus import EventBus
from org.vertx.java.core.json import JsonArray
from org.vertx.java.core.json import JsonObject



def handler(msg):
    print 'Received message'
    flights = JsonArray()
    for flight in msg.body:
        the_flight = JsonObject()
        the_flight.putString("name", flight.getString("callsign"))
        the_flight.putString("planeType", flight.getString("equipment") )
        position_array =  flight.getArray("positions").toArray()

        #There can sometimes be two positions readings but I am not sure what they do so I am just going to take the first
        position =  position_array[0]

        the_flight.putNumber("lat", position.get("lat"))
        the_flight.putNumber("lon", position.get("lon"))
        the_flight.putNumber("speed", position.get("speedMph"))
        the_flight.putNumber("alt",  position.get("altitudeFt"))


        flights.addObject(the_flight)

    #Sent the array on the EventBus - this is the one the page should subscribe to
    EventBus.publish('flights.updated', flights)
    print("published the flights")


EventBus.register_handler('raw.flights', handler=handler)