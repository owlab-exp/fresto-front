package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.*;

import views.html.*;

import java.util.*;

import org.apache.thrift.TSerializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;


import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import fresto.format.UIEvent;
import fresto.Global;

public class Application extends Controller {
 	private static String pubHost = "localhost";
	private static int pubPort = 7000;

    public static Result index() {
        //return ok(index.render("Your new application is ready."));
	StringBuffer sb = new StringBuffer();
	sb.append("Under construction. \n Urls to give or get events");
	sb.append("\n");
	sb.append("http://fresto1.owlab.com:9999/feedUIEvent");
	sb.append("\n");
	sb.append("http://fresto1.owlab.com:9999/getR0");
        return ok(sb.toString());
    }

    //@BodyParser.Of(BodyParser.Json.class)
    //public static Result feedUIEvent() {
    //        RequestBody body = request().body();
    //        Logger.info("UI Event = " + body);
    //        Logger.info("UI Event = " + body.asJson());
    //        return ok("RECV_OK");
    //}
  
    public static Result whatIsMyIPAddress() {
	    String remote = request().remoteAddress();
	    String result = "getip({ \"ip\":\""+ remote +"\" });";
	    Logger.info("result: " + result);
	    return ok(result);

    }


    public static Result feedUIEvent() {
	    DynamicForm data = Form.form().bindFromRequest();
	    String stage = data.get("stage");
	    String clientId = data.get("clientId");
	    String currentPlace = data.get("currentPlace");
	    String uuid = data.get("uuid");
	    String targetUrl = data.get("targetUrl");
	    String timestamp = data.get("timestamp");
	    String elapsedTime = data.get("elapsedTime");

	    Logger.info(stage + "," + clientId + "," + currentPlace + "," + uuid + "," + targetUrl + "," + timestamp + "," + elapsedTime); 
	    


	    TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());
	    UIEvent event = new UIEvent(stage, clientId, uuid, targetUrl, Long.parseLong(timestamp));
	    if(currentPlace != null)
	    	event.setCurrentPlace(currentPlace);
	    if(elapsedTime != null) 
	    	event.setElapsedTime(Long.parseLong(elapsedTime));

	    try {
	    	byte[] serializedEvent = serializer.serialize(event);
		//ZMQ.Socket publisher = Global.getPublisher();
		//publisher.send("U".getBytes(), ZMQ.SNDMORE);
		//publisher.send(serializedEvent, 0);
		Global.publishToMonitor("U", serializedEvent);
	    } catch (TException te) {
		    te.printStackTrace();
	    } finally {
		    //publisher.close();
		    //context.term();
	    }

	    return ok("RECV_OK");
    }
}
