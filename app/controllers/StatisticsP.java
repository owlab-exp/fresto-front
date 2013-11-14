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

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
//import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
//import com.orientechnologies.orient.core.record.impl.ODocument;
//import com.orientechnologies.orient.core.db.graph.OGraphDatabasePool;
//import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
//import com.orientechnologies.orient.core.metadata.schema.OType;

import fresto.Global;

public class StatisticsP extends Controller {
	//private static OGraphDatabasePool oGraphPool = OGraphDatabasePool.global();

	//final static ActorRef r0Actor = R0Actor.instance;

	@BodyParser.Of(BodyParser.Json.class)
	public static Result getUniqueClientCount(String callback) {
		Logger.info("getUniqueClientCount:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();

		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);

			int count = getUniqueCount(secondInUnix, "Request", "clientIp");
			dataObject.put("u0", count);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientHitCount(String callback) {
		Logger.info("clientHitCount:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();

		if(json == null) {
			Logger.info("Body is not JSON");	
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			//dataObject.put("h0", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "Request");
			dataObject.put("h0", hitCount);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientThroughput(String callback) {
		Logger.info("clientThroughput:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			//dataObject.put("h0", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "Response");
			dataObject.put("t0", hitCount);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpHitCount(String callback) {
		Logger.info("httpHitCount:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			result.put("status", "KO");
			result.put("message", "Body is not json");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");
		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "EntryCall");
			dataObject.put("h1", hitCount);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpThroughput(String callback) {
		Logger.info("httpThroughput:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "EntryReturn");
			dataObject.put("t1", hitCount);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlHitCount(String callback) {
		Logger.info("sqlHitCount:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			result.put("status", "KO");
			result.put("message", "Body is not json");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");
		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "SqlCall");
			dataObject.put("h9", hitCount);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlThroughput(String callback) {
		Logger.info("sqlThroughput:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "SqlReturn");
			dataObject.put("t9", hitCount);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientResponseTimes(String callback) {
		Logger.info("clientResponseTimes:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			ArrayNode responseTimeArray = dataObject.putArray("responseTimes");
			//for(int i = 0; i < clientCount; i++) {
			//	ObjectNode responseTimeObject = responseTimeArray.addObject();
			//	responseTimeObject.put("rid", i);
			//	responseTimeObject.put("r0", responseTimeRandom.nextInt(3000));
			//}
			getResponseTimes(secondInUnix, "Response", "r0", responseTimeArray);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpResponseTimes(String callback) {
		Logger.info("httpResponseTimes:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			ArrayNode responseTimeArray = dataObject.putArray("responseTimes");
			//for(int i = 0; i < clientCount; i++) {
			//	ObjectNode responseTimeObject = responseTimeArray.addObject();
			//	responseTimeObject.put("rid", i);
			//	responseTimeObject.put("r1", responseTimeRandom.nextInt(3000));
			//}
			getResponseTimes(secondInUnix, "EntryReturn", "r1", responseTimeArray);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlResponseTimes(String callback) {
		Logger.info("sqlResponseTimes:callback called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			ArrayNode responseTimeArray = dataObject.putArray("responseTimes");
			//for(int i = 0; i < clientCount; i++) {
			//	ObjectNode responseTimeObject = responseTimeArray.addObject();
			//	responseTimeObject.put("rid", i);
			//	responseTimeObject.put("r1", responseTimeRandom.nextInt(3000));
			//}
			getResponseTimes(secondInUnix, "SqlReturn", "r9", responseTimeArray);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result getElapsedTimeStatistics(String callback) {
		Logger.info("getElapsedTimeStatistics:callback called.");
		response().setHeader("Access-Control-Allow-Origin", "*");

		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(Jsonp.jsonp(callback, result));
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode targetNode = json.findPath("target");

		if(secondNode.isMissingNode() || targetNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [target]");
			return badRequest(Jsonp.jsonp(callback, result));
		} else if(!targetNode.getTextValue().equals("response")  
				&& !targetNode.getTextValue().equals("entryReturn")
				&& !targetNode.getTextValue().equals("operationReturn") 
				&& !targetNode.getTextValue().equals("sqlReturn")) {
			result.put("status", "KO");
			result.put("message", "Possible targets : response, entryReturn, operationReturn, sqlReturn");
			return badRequest(Jsonp.jsonp(callback, result));

		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			String target = targetNode.getTextValue();

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);

			if(target.equalsIgnoreCase("response")) 
				target = "Response";
			else if(target.equalsIgnoreCase("entryReturn"))
				target = "EntryReturn";
			else if(target.equalsIgnoreCase("operationReturn"))
				target = "OperationReturn";
			else if(target.equalsIgnoreCase("sqlReturn"))
				target = "SqlReturn";

			dataObject.put("target", target);
			ObjectNode innObject = dataObject.putObject("statistics");
			//for(int i = 0; i < clientCount; i++) {
			//	ObjectNode responseTimeObject = responseTimeArray.addObject();
			//	responseTimeObject.put("rid", i);
			//	responseTimeObject.put("r1", responseTimeRandom.nextInt(3000));
			//}
			getElapsedTimeStatistics(secondInUnix, target, innObject);

			return ok(Jsonp.jsonp(callback, result));
		}
	}

	private static int getCount(long secondInMillis, String target) {
		TitanGraph g = Global.getGraph();

		int count = 0;
		long second = (secondInMillis/1000) * 1000;

		Iterator<Vertex> it = g.getVertices("second", second).iterator();
		Vertex v = null;
		if(it.hasNext()) {
			v = it.next();
			count = (int) v.query().labels("include").has("event", target).count();
		}

		return count;
		//OGraphDatabase oGraph = Global.openDatabase();
		//oGraph.setLockMode(OGraphDatabase.LOCK_MODE.NO_LOCKING);

		//int count = 0;
		//try {
		//	long minute = (secondInMillis/60000) * 60000;
	
		//	OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>();
		//	oQuery.setText("SELECT expand(second[" + secondInMillis + "]." + target + ") FROM TSRoot where minute = " + minute);
		//	List<ODocument> result = oGraph.query(oQuery);
		//	count = result.size();
		//} finally {
		//	oGraph.close();
		//}

		//return count;
	}

	private static int getUniqueCount(long secondInMillis, String target, String fieldOfTarget) {
		TitanGraph g = Global.getGraph();

		int count = 0;
		long second = (secondInMillis/1000) * 1000;

		Iterator<Vertex> it = g.getVertices("second", second).iterator();
		Vertex v = null;
		Set<Object> strSet = new HashSet<Object>();
		if(it.hasNext()) {
			v = it.next();
			for(Vertex vertex : v.query().labels("include").has("event", target).vertices()) {
				strSet.add(vertex.getProperty(fieldOfTarget));
			}
			count = strSet.size();
		}
		return count;

		//try {
	
		//	OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>();
		//	oQuery.setText("SELECT distinct("+ fieldOfTarget + ") FROM (SELECT expand(second[" + secondInMillis + "]." + target + ") FROM TSRoot where minute = " + minute + ")");
		//	List<ODocument> result = oGraph.query(oQuery);
		//	count = result.size();
		//} finally {
		//	oGraph.close();
		//}

		//return count;
	}

	private static void getResponseTimes(long secondInMillis, String target, String responseTimeTag, ArrayNode responseTimeArray) {
		TitanGraph g = Global.getGraph();

		long second = (secondInMillis/1000) * 1000;

		Iterator<Vertex> it = g.getVertices("second", second).iterator();
		Vertex v = null;
		if(it.hasNext()) {
			v = it.next();
			for(Vertex vertex : v.query().labels("include").has("event", target).vertices()) {
				ObjectNode responseTimeObject = responseTimeArray.addObject();
				responseTimeObject.put("rid", (Long) vertex.getId());
				responseTimeObject.put(responseTimeTag, (Integer) vertex.getProperty("elapsedTime"));
			}
		}


		//OGraphDatabase oGraph = Global.openDatabase();
		//oGraph.setLockMode(OGraphDatabase.LOCK_MODE.NO_LOCKING);

		//try {
		//	long minute = (secondInMillis/60000) * 60000;
		//	OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>();
		//	oQuery.setText("SELECT expand(second[" + secondInMillis + "]." + target + ") FROM TSRoot where minute = " + minute);
		//	List<ODocument> result = oGraph.query(oQuery);

		//	int count = result.size();
		//	for(int i = 0; i < count; i++) {
		//		ODocument aDoc = result.get(0);
		//		ObjectNode responseTimeObject = responseTimeArray.addObject();
		//		responseTimeObject.put("rid", aDoc.getIdentity().toString());
		//		responseTimeObject.put(responseTimeTag, (long)aDoc.field("elapsedTime"));
		//	}
		//} finally {
		//	oGraph.close();
		//}
	}

	private static void getElapsedTimeStatistics(long secondInMillis, String target, ObjectNode objectNode) {
		TitanGraph g = Global.getGraph();

		long second = (secondInMillis/1000) * 1000;

		Iterator<Vertex> it = g.getVertices("second", second).iterator();
		Vertex v = null;
		SortedSet<Integer> numSet = new TreeSet<Integer>();
		int value = 0;
		int sum = 0;
		int count = 0;
		if(it.hasNext()) {
			v = it.next();
			for(Vertex vertex : v.query().labels("include").has("event", target).vertices()) {
				count++;
				value = (Integer) vertex.getProperty("elapsedTime");
				sum += value;
				numSet.add(value);
			}
		}


		objectNode.put("cnt", count);
		if(count > 0) {
			objectNode.put("avg", sum/count);
			objectNode.put("min", numSet.first());
			objectNode.put("max", numSet.last());
		} else {
			objectNode.put("avg", 0);
			objectNode.put("min", 0);
			objectNode.put("max", 0);
		}


		//OGraphDatabase oGraph = Global.openDatabase();
		//oGraph.setLockMode(OGraphDatabase.LOCK_MODE.NO_LOCKING);
		//
		//try {
		//	long minute = (secondInMillis/60000) * 60000;

		//	OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>();
		//	oQuery.setText("SELECT count(elapsedTime) as cnt, avg(elapsedTime), min(elapsedTime), max(elapsedTime) from (SELECT expand(second[" + secondInMillis + "]." + target + ") FROM TSRoot where minute = " + minute + ")");
		//	List<ODocument> result = oGraph.query(oQuery);
		//	if(result.size() > 0) {
		//		if(result.get(0).field("cnt") != null)
		//			objectNode.put("cnt", (long)result.get(0).field("cnt"));
		//		else 
		//			objectNode.put("cnt", 0);

		//		if(result.get(0).field("avg") != null)
		//			objectNode.put("avg", (long)result.get(0).field("avg"));
		//		else 
		//			objectNode.put("avg", 0);

		//		if(result.get(0).field("min") != null)
		//			objectNode.put("min", (long)result.get(0).field("min"));
		//		else 
		//			objectNode.put("min", 0);

		//		if(result.get(0).field("max") != null)
		//			objectNode.put("max", (long)result.get(0).field("max"));
		//		else 
		//			objectNode.put("max", 0);
		//	}
		//} finally {
		//	oGraph.close();
		//}
	}
}
