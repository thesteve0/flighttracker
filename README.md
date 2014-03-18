# Vert.x Example Maven Project which has Java, Python, and a spatial feed.

Prequisites:
1) You need an OpenShift account
2) You need a [https://developer.flightstats.com/](FlightStats account)
3) You need Maven installed on your local machine

Create a new OpenShift application with a vert.x cartridge

    rhc app create flighttracker jboss-vertx-2.1 -g medium -s

For best performance and flexibility I used a medium gear and I made it scalable.

You will need to add your FlightStats environment variables to the application after creation

    rhc set-env set OPENSHIFT_FLIGHTSTATS_ID=<app id>
    rhc set-env set OPENSHIFT_FLIGHTSTATS_KEY=<key>


Then clone this repo locally.

You need to have Maven installed to build this project.

Then inside the root of the repository do:

    mvn package

This will package up the project into a module. We used a module because there are three
different languages used in the application, JavaScript, Java, and Python. This wasn't required to make
the project but it was more to demonstrate the polyglot nature of vert.x

To deploy the application go into the new directory called _target_. Take the
directory titled "com.openshift~vertx-tracking~0.1" and copy it into the _mod_ directory inside the
git repository _flighttracker_. Then inside the git repository go into the _configuration_ directory
and edit _vertx.env_ file.

Uncomment export vertx_module and make it like this:

  export vertx_module=com.openshift~vertx-tracking~0.1

and comment out the rest of the export lines.

Remove server.js from the git repository.

Now you can do

    git add --all .
    git commit -am "your message"
    git push

When this is finished you should be able to go to

flighttracker-<yourdomain>.rhcloud.com

Remember you may have to wait a few seconds for the first pins to show up because pins aren't drawn
until a set of flights is pushed from the EventBus to index.html
