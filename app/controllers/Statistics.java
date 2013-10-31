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

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.db.graph.OGraphDatabasePool;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.metadata.schema.OType;

import fresto.Global;

public class Statistics extends Controller {
	private static OGraphDatabasePool oGraphPool = OGraphDatabasePool.global();

	//final static ActorRef r0Actor = R0Actor.instance;

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientHitCount() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
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
			//dataObject.put("h0", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "request");
			dataObject.put("h0", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientThroughput() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
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
			//dataObject.put("h0", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "response");
			dataObject.put("t0", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpHitCount() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			result.put("status", "KO");
			result.put("message", "Body is not json");
			return badRequest(result);
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
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "entryCall");
			dataObject.put("h1", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpThroughput() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
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
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "entryReturn");
			dataObject.put("t1", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlHitCount() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			result.put("status", "KO");
			result.put("message", "Body is not json");
			return badRequest(result);
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
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "sqlCall");
			dataObject.put("h9", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlThroughput() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
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
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "sqlReturn");
			dataObject.put("t9", hitCount);

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
			getResponseTimes(secondInUnix, "response", "r0", responseTimeArray);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpResponseTimes() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(result);
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
			getResponseTimes(secondInUnix, "entryReturn", "r1", responseTimeArray);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlResponseTimes() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(result);
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
			getResponseTimes(secondInUnix, "sqlReturn", "r9", responseTimeArray);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result getElapsedTimeStatistics() {
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		JsonNode secondNode = json.findPath("second");
		JsonNode targetNode = json.findPath("target");

		if(secondNode.isMissingNode() || targetNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [target]");
			return badRequest(result);
		} else if(!targetNode.getTextValue().equals("response")  
				&& !targetNode.getTextValue().equals("entryReturn")
				&& !targetNode.getTextValue().equals("operationReturn") 
				&& !targetNode.getTextValue().equals("sqlReturn")) {
			result.put("status", "KO");
			result.put("message", "Possible targets : response, entryReturn, operationReturn, sqlReturn");
			return badRequest(result);

		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			String target = targetNode.getTextValue();

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("target", target);
			ObjectNode innObject = dataObject.putObject("statistics");
			//for(int i = 0; i < clientCount; i++) {
			//	ObjectNode responseTimeObject = responseTimeArray.addObject();
			//	responseTimeObject.put("rid", i);
			//	responseTimeObject.put("r1", responseTimeRandom.nextInt(3000));
			//}
			getElapsedTimeStatistics(secondInUnix, target, innObject);

			return ok(result);
		}
	}

	private static int getCount(long secondInMillis, String target) {

		//OGraphDatabase oGraph = oGraphPool.acquire("remote:fresto3.owlab.com/frestodb", "admin", "admin");
		//OGraphDatabase oGraph = OGraphDatabasePool.global().acquire("remote:fresto3.owlab.com/frestodb", "admin", "admin");
		OGraphDatabase oGraph = Global.openDatabase();
		oGraph.setLockMode(OGraphDatabase.LOCK_MODE.NO_LOCKING);

		int count = 0;
		try {
			long minute = (secondInMillis/60000) * 60000;
	
			OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>();
			oQuery.setText("SELECT expand(second[" + secondInMillis + "]." + target + ") FROM TSRoot where minute = " + minute);
			List<ODocument> result = oGraph.query(oQuery);
			count = result.size();
		} finally {
			oGraph.close();
		}

		return count;
	}

	private static void getResponseTimes(long secondInMillis, String target, String responseTimeTag, ArrayNode responseTimeArray) {
		//OGraphDatabase oGraph = oGraphPool.acquire("remote:fresto3.owlab.com/frestodb", "admin", "admin");
		OGraphDatabase oGraph = Global.openDatabase();
		oGraph.setLockMode(OGraphDatabase.LOCK_MODE.NO_LOCKING);

		try {
			long minute = (secondInMillis/60000) * 60000;
			OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>();
			oQuery.setText("SELECT expand(second[" + secondInMillis + "]." + target + ") FROM TSRoot where minute = " + minute);
			List<ODocument> result = oGraph.query(oQuery);

			int count = result.size();
			for(int i = 0; i < count; i++) {
				ODocument aDoc = result.get(0);
				ObjectNode responseTimeObject = responseTimeArray.addObject();
				responseTimeObject.put("rid", aDoc.getIdentity().toString());
				responseTimeObject.put(responseTimeTag, (long)aDoc.field("elapsedTime"));
			}
		} finally {
			oGraph.close();
		}
	}

	private static void getElapsedTimeStatistics(long secondInMillis, String target, ObjectNode objectNode) {
		//OGraphDatabase oGraph = oGraphPool.acquire("remote:fresto3/frestodb", "admin", "admin");
		OGraphDatabase oGraph = Global.openDatabase();
		oGraph.setLockMode(OGraphDatabase.LOCK_MODE.NO_LOCKING);
		
		try {
			long minute = (secondInMillis/60000) * 60000;

			OSQLSynchQuery<ODocument> oQuery = new OSQLSynchQuery<ODocument>();
			oQuery.setText("SELECT count(elapsedTime) as cnt, avg(elapsedTime), min(elapsedTime), max(elapsedTime) from (SELECT expand(second[" + secondInMillis + "]." + target + ") FROM TSRoot where minute = " + minute + ")");
			List<ODocument> result = oGraph.query(oQuery);
			if(result.size() > 0) {
				if(result.get(0).field("cnt") != null)
					objectNode.put("cnt", (long)result.get(0).field("cnt"));
				else 
					objectNode.put("cnt", 0);

				if(result.get(0).field("avg") != null)
					objectNode.put("avg", (long)result.get(0).field("avg"));
				else 
					objectNode.put("avg", 0);

				if(result.get(0).field("min") != null)
					objectNode.put("min", (long)result.get(0).field("min"));
				else 
					objectNode.put("min", 0);

				if(result.get(0).field("max") != null)
					objectNode.put("max", (long)result.get(0).field("max"));
				else 
					objectNode.put("max", 0);
			}
		} finally {
			oGraph.close();
		}
	}
}
