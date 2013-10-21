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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import views.html.*;

import java.util.*;
import static java.util.concurrent.TimeUnit.*;

//import org.apache.thrift.TDeserializer;
//import org.apache.thrift.TException;
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TBinaryProtocol.Factory;


//import org.zeromq.ZMQ;
//import org.zeromq.ZMQ.Context;
//import org.zeromq.ZMQ.Socket;

//import fresto.data.FrestoData;
//import fresto.Global;
//import fresto.libs.EventSource;

public class Statistics extends Controller {

	//final static ActorRef r0Actor = R0Actor.instance;

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpHitCount() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(result);
		} else {
			long secondInUnix = secondNode.getLongValue();

			result.put("status", "OK");
			Random random = new Random();
			int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("h1", hitCount);
			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientResponseTimes() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(result);
		} else {
			long secondInUnix = secondNode.getLongValue();

			result.put("status", "OK");

			Random clientCountRandom = new Random();
			Random responseTimeRandom = new Random();

			int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			ArrayNode responseTimeArray = dataObject.putArray("responseTimes");
			for(int i = 0; i < clientCount; i++) {
				ObjectNode responseTimeObject = responseTimeArray.addObject();
				responseTimeObject.put("rid", i);
				responseTimeObject.put("r0", responseTimeRandom.nextInt(3000));
			}
			
			return ok(result);
		}
	}

}
