/**************************************************************************************
 * Copyright 2013 TheSystemIdeas, Inc and Contributors. All rights reserved.          *
 *                                                                                    *
 *     https://github.com/owlab/fresto                                                *
 *                                                                                    *
 *                                                                                    *
 * ---------------------------------------------------------------------------------- *
 * This file is licensed under the Apache License, Version 2.0 (the "License");       *
 * you may not use this file except in compliance with the License.                   *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 * 
 **************************************************************************************/
package controllers;

import play.*;
import play.mvc.*;
import play.libs.*;
import play.libs.F.*;

import akka.util.*;
import akka.actor.*;

import java.util.*;
import java.text.*;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.*;

import scala.concurrent.ExecutionContext$;

import views.html.*;


import fresto.libs.EventSource;

public class ClockTutorial extends Controller {
    
    final static ActorRef clock = Clock.instance;

    public static Result liveClock() {
        return ok(new EventSource() {  
            public void onConnected() {
               clock.tell(this, null); 
            } 
        });
    }
    
    public static class Clock extends UntypedActor {
        
        static ActorRef instance = Akka.system().actorOf(new Props(Clock.class));
        
        // Send a TICK message every 100 millis
        static {
            Akka.system().scheduler().schedule(
                Duration.Zero(),
                Duration.create(100, MILLISECONDS),
                instance, "TICK",  Akka.system().dispatcher());
           //     instance, "TICK",  Akka.system().dispatcher(),
           //     null
           // );
        }
        
        List<EventSource> sockets = new ArrayList<EventSource>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm ss");
        
        public void onReceive(Object message) {

            // Handle connections
            if(message instanceof EventSource) {
                final EventSource eventSource = (EventSource)message;
                
                if(sockets.contains(eventSource)) {                    
                    // Brower is disconnected
                    sockets.remove(eventSource);
                    Logger.info("Browser disconnected (" + sockets.size() + " browsers currently connected)");
                    
                } else {                    
                    // Register disconnected callback 
                    eventSource.onDisconnected(new Callback0() {
                        public void invoke() {
                            getContext().self().tell(eventSource, null);
                        }
                    });                    
                    // New browser connected
                    sockets.add(eventSource);
                    Logger.info("New browser connected (" + sockets.size() + " browsers currently connected)");
                    
                }
                
            }             
            // Tick, send time to all connected browsers
            if("TICK".equals(message)) {
		//    Logger.info("TICK arrived");
                // Send the current time to all EventSource sockets
                List<EventSource> shallowCopy = new ArrayList<EventSource>(sockets); //prevent ConcurrentModificationException
                for(EventSource es: shallowCopy) {
                    es.sendData(dateFormat.format(new Date()));
                }
                
            }

        }
        
    }
  
}
