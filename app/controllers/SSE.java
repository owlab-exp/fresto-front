package controllers;

import scala.concurrent.duration.Duration;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.*;
import play.libs.F.*;

import akka.util.*;
import akka.actor.*;

import org.codehaus.jackson.node.ObjectNode;

import views.html.*;

import java.util.*;
import static java.util.concurrent.TimeUnit.*;


import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;


import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import fresto.format.UIEvent;
import fresto.Global;
import fresto.libs.EventSource;

public class SSE extends Controller {

	final static ActorRef r0Actor = R0Actor.instance;

	public static Result r0Stream() {
		return ok(new EventSource() {
			public void onConnected() {
				r0Actor.tell(this, null);
			}
		});
	}

	public static class R0Actor extends UntypedActor {
		static ActorRef instance = Akka.system().actorOf(new Props(R0Actor.class));

		static {
			Akka.system().scheduler().scheduleOnce(
					Duration.Zero(),
					new Runnable() {
						public void run() {
							ZMQ.Context context = ZMQ.context(1);
							ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
							subscriber.connect("tcp://fresto1.owlab.com:7001");
							subscriber.subscribe("U".getBytes());

							TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
							UIEvent event = new UIEvent();
							while(true) {
								String envelope = new String(subscriber.recv(0));
								byte[] messageBytes = subscriber.recv(0);
								
								try {
									event.clear();
									deserializer.deserialize(event, messageBytes);

									ObjectNode jsonObject = Json.newObject();
									jsonObject.put("stage", event.getStage());
									jsonObject.put("clientId", event.getClientId());
									jsonObject.put("currentPlace", event.getCurrentPlace());
									jsonObject.put("uuid", event.getUuid());
									jsonObject.put("url", event.getUrl());
									jsonObject.put("httpStatus", event.getHttpStatus());
									jsonObject.put("timestamp", event.getTimestamp());
									jsonObject.put("elapsedTime", event.getElapsedTime());

									//sendData(event.getStage() + "," + event.getElapsedTime());
									// To send with JSON string
									//sendJsonData(Json.stringify(Json.toJson(jsonObject)));
									//
									//instance.tell(Json.stringify(Json.toJson(jsonObject)));
									instance.tell(jsonObject);

								} catch (TException te) {
									te.printStackTrace();
								} finally {
									//close();
								}
							}
						}
					},
					Akka.system().dispatcher());
		}

		 static { 
			 Akka.system().scheduler().schedule( 
					 Duration.Zero(), 
					 Duration.create(100, MILLISECONDS), 
					 instance, "PING",  Akka.system().dispatcher());
		 }

		List<EventSource> sockets = new ArrayList<EventSource>();
		
		public void onReceive(Object message) {
			if(message instanceof EventSource) {
				final EventSource eventSource = (EventSource)message;

				if(sockets.contains(eventSource)) {
					sockets.remove(eventSource);
					Logger.info("Browser disconnected (" + sockets.size() + " brwosers currently connected)");
				} else {
					eventSource.onDisconnected(new Callback0() {
						public void invoke() {
							getContext().self().tell(eventSource, null);
						}
					});

					sockets.add(eventSource);
					Logger.info("New browser connected (" + sockets.size() + " browsers currently connected)");
				}
			}

			//if(message instanceof String) {
			//	List<EventSource> shallowCopy = new ArrayList<EventSource>(sockets); // prevent concurrent modification exception
			//	for(EventSource es: shallowCopy) {
			//		es.sendJsonData((String) message);
			//	}
			//}

			// To be more clear 
			if(message instanceof ObjectNode) {
				List<EventSource> shallowCopy = new ArrayList<EventSource>(sockets); // prevent concurrent modification exception
				//List<EventSource> shallowCopy = Collections.synchronizedList(sockets); // prevent concurrent modification exception
				for(EventSource es: shallowCopy) {
					es.sendJsonData(Json.stringify(Json.toJson(message)));
				}
			}

			// To detect clients disconnected
			if("PING".equals(message)) {
				List<EventSource> shallowCopy = new ArrayList<EventSource>(sockets); // prevent concurrent modification exception
				//List<EventSource> shallowCopy = Collections.synchronizedList(sockets); // prevent concurrent modification exception
				for(EventSource es: shallowCopy) {
					es.sendDataByName("ping", "");
				}
				
			}
		}
	}

}
