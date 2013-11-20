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

public class Statistics extends Controller {
	//private static OGraphDatabasePool oGraphPool = OGraphDatabasePool.global();

	//final static ActorRef r0Actor = R0Actor.instance;

	@BodyParser.Of(BodyParser.Json.class)
	public static Result getUniqueClientCount() {
		Logger.info("getUniqueClientCount called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();

		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
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

			int count = getUniqueCount(secondInUnix, "Request", "clientIp");
			dataObject.put("u0", count);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result getUniqueClientCountForSeconds() {
		Logger.info("getUniqueClientCountForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();

		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);

			int count = getUniqueCountForSeconds(secondInUnix, durationSeconds, "Request", "clientIp");
			dataObject.put("u0", count);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientHitCount() {
		Logger.info("clientHitCount called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();

		if(json == null) {
			Logger.info("Body is not JSON");	
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			Logger.info("second=" + secondInUnix);

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

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientHitCountForSeconds() {
		Logger.info("clientHitCountForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();

		if(json == null) {
			Logger.info("Body is not JSON");	
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			//dataObject.put("h0", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCountForSeconds(secondInUnix, durationSeconds, "Request");
			dataObject.put("h0", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientThroughput() {
		Logger.info("clientThroughput called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
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
			//dataObject.put("h0", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCount(secondInUnix, "Response");
			dataObject.put("t0", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientThroughputForSeconds() {
		Logger.info("clientThroughputForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			//dataObject.put("h0", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCountForSeconds(secondInUnix, durationSeconds, "Response");
			dataObject.put("t0", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpHitCount() {
		Logger.info("httpHitCount called.");
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

			int hitCount = getCount(secondInUnix, "EntryOperationCall");
			dataObject.put("h1", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpHitCountForSeconds() {
		Logger.info("httpHitCountForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			result.put("status", "KO");
			result.put("message", "Body is not json");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCountForSeconds(secondInUnix, durationSeconds, "EntryOperationCall");
			dataObject.put("h1", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpThroughput() {
		Logger.info("httpThroughput called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
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

			int hitCount = getCount(secondInUnix, "EntryOperationReturn");
			dataObject.put("t1", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpThroughputForSeconds() {
		Logger.info("httpThroughputForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duratioin]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCountForSeconds(secondInUnix, durationSeconds, "EntryOperationReturn");
			dataObject.put("t1", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlHitCount() {
		Logger.info("sqlHitCount called.");
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

			int hitCount = getCount(secondInUnix, "SqlCall");
			dataObject.put("h9", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlHitCountForSeconds() {
		Logger.info("sqlHitCountForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			result.put("status", "KO");
			result.put("message", "Body is not json");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCountForSeconds(secondInUnix, durationSeconds, "SqlCall");
			dataObject.put("h9", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlThroughput() {
		Logger.info("sqlThroughput called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
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

			int hitCount = getCount(secondInUnix, "SqlReturn");
			dataObject.put("t9", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlThroughputForSeconds() {
		Logger.info("sqlThroughputForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");
			//Random random = new Random();
			//int hitCount = random.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			//dataObject.put("h1", hitCount);
			//ObjectNode dataObject = result.putObject("data");
			//dataObject.put("second", secondInUnix);

			int hitCount = getCountForSeconds(secondInUnix, durationSeconds, "SqlReturn");
			dataObject.put("t9", hitCount);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientResponseTimes() {
		Logger.info("clientResponseTimes called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
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

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result clientResponseTimesForSeconds() {
		Logger.info("clientResponseTimesForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			ArrayNode responseTimeArray = dataObject.putArray("responseTimes");
			//for(int i = 0; i < clientCount; i++) {
			//	ObjectNode responseTimeObject = responseTimeArray.addObject();
			//	responseTimeObject.put("rid", i);
			//	responseTimeObject.put("r0", responseTimeRandom.nextInt(3000));
			//}
			getResponseTimesForSeconds(secondInUnix, durationSeconds, "Response", "r0", responseTimeArray);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpResponseTimes() {
		Logger.info("httpResponseTimes called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
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
			getResponseTimes(secondInUnix, "EntryOperationReturn", "r1", responseTimeArray);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result httpResponseTimesForSeconds() {
		Logger.info("httpResponseTimesForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			ArrayNode responseTimeArray = dataObject.putArray("responseTimes");
			//for(int i = 0; i < clientCount; i++) {
			//	ObjectNode responseTimeObject = responseTimeArray.addObject();
			//	responseTimeObject.put("rid", i);
			//	responseTimeObject.put("r1", responseTimeRandom.nextInt(3000));
			//}
			getResponseTimesForSeconds(secondInUnix, durationSeconds, "EntryOperationReturn", "r1", responseTimeArray);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlResponseTimes() {
		Logger.info("sqlResponseTimes called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
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

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result sqlResponseTimesForSeconds() {
		Logger.info("sqlResponseTimesForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");

		if(secondNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration]");
			return badRequest(result);
		} else {
			long secondInUnix = (secondNode.getLongValue()/1000) * 1000;
			int durationSeconds = durationNode.getIntValue();

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);
			ArrayNode responseTimeArray = dataObject.putArray("responseTimes");
			//for(int i = 0; i < clientCount; i++) {
			//	ObjectNode responseTimeObject = responseTimeArray.addObject();
			//	responseTimeObject.put("rid", i);
			//	responseTimeObject.put("r1", responseTimeRandom.nextInt(3000));
			//}
			getResponseTimesForSeconds(secondInUnix, durationSeconds, "SqlReturn", "r9", responseTimeArray);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result getElapsedTimeStatistics() {
		Logger.info("getElapsedTimeStatistics called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

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

			if(target.equalsIgnoreCase("response")) 
				target = "Response";
			else if(target.equalsIgnoreCase("entryReturn"))
				target = "EntryOperationReturn";
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

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result getElapsedTimeStatisticsForSeconds() {
		Logger.info("getElapsedTimeStatisticsForSeconds called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode secondNode = json.findPath("second");
		JsonNode durationNode = json.findPath("duration");
		JsonNode targetNode = json.findPath("target");

		if(secondNode.isMissingNode() || targetNode.isMissingNode() || durationNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [second] or [duration] or [target]");
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
			int durationSeconds = durationNode.getIntValue();
			String target = targetNode.getTextValue();

			result.put("status", "OK");

			//Random clientCountRandom = new Random();
			//Random responseTimeRandom = new Random();

			//int clientCount = clientCountRandom.nextInt(300);
			ObjectNode dataObject = result.putObject("data");
			dataObject.put("second", secondInUnix);
			dataObject.put("duration", durationSeconds);

			if(target.equalsIgnoreCase("response")) 
				target = "Response";
			else if(target.equalsIgnoreCase("entryReturn"))
				target = "EntryOperationReturn";
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
			getElapsedTimeStatisticsForSeconds(secondInUnix, durationSeconds, target, innObject);

			return ok(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result getResponseTimeDetail() {
		Logger.info("getResponseTimeDetail called.");
		ObjectNode result = Json.newObject();
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			Logger.info("Body is not JSON");
			result.put("status", "KO");
			result.put("message","Not JSON body");
			return badRequest(result);
		}

		JsonNode idNode = json.findPath("rid");

		if(idNode.isMissingNode()) {
			result.put("status", "KO");
			result.put("message", "Missing parameter [rid]");
			return badRequest(result);
		} 

		result.put("status", "OK");

		long id = idNode.getLongValue();
		ObjectNode dataObject = result.putObject("data");
		boolean isSuccess = getElapsedTimeDetail(id, dataObject);
		if(!isSuccess) {
			result.remove("status");
			result.remove("data");
			result.put("status", "KO");
			result.put("message", "The ID is not valid or nor valid results");
			return badRequest(result);
		}
		return ok(result);
	}

	private static int getCountForSeconds(long secondInMillis, int previousSeconds, String target) {
		int count = 0;
		long second = (secondInMillis/1000) * 1000;

		//for(int i = 0; i < previousSeconds; i++) {
		for(int i = (previousSeconds - 1); i > -1; i--) {
			//Logger.info("previousSeconds=" + (second - (i * 1000)));
			count += getCount(second - (i * 1000), target);
		}
		return count;
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
			Logger.info("Number of vertices of this second/target: " + count);
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

	private static int getUniqueCountForSeconds(long secondInMillis, int previousSeconds, String target, String fieldOfTarget) {
		TitanGraph g = Global.getGraph();

		int count = 0;
		long second = (secondInMillis/1000) * 1000;

		Set<Object> strSet = new HashSet<Object>();
		Vertex v = null;

		//for(int i = 0; i < previousSeconds; i++) {
		for(int i = (previousSeconds - 1); i > -1; i--) {
			//count += getUniqueCount(second - (i * 1000), target, fieldOfTarget);
			//Logger.info("previousSeconds=" + (second - (i * 1000)));
			Iterator<Vertex> it = g.getVertices("second", second - (i * 1000)).iterator();
			if(it.hasNext()) {
				v = it.next();
				for(Vertex vertex : v.query().labels("include").has("event", target).vertices()) {
					strSet.add(vertex.getProperty(fieldOfTarget));
				}
			}
		}
		count = strSet.size();
		Logger.info("Number of vertices for " + previousSeconds + " seconds: " + count);

		return count;
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
			Logger.info("Number of vertices of this second: " + count);
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

	private static void getResponseTimesForSeconds(long secondInMillis, int previousSeconds, String target, String responseTimeTag, ArrayNode responseTimeArray) {
		long second = (secondInMillis/1000) * 1000;
		//for(int i = 0; i < previousSeconds; i++) {
		for(int i = (previousSeconds - 1); i > -1; i--) {
			//Logger.info("previousSeconds=" + (second - (i * 1000)));
			getResponseTimes(second - (i * 1000), target, responseTimeTag, responseTimeArray);
		}
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

	private static void getElapsedTimeStatisticsForSeconds(long secondInMillis, int previousSeconds, String target, ObjectNode objectNode) {
		TitanGraph g = Global.getGraph();
		long second = (secondInMillis/1000) * 1000;

		SortedSet<Integer> numSet = new TreeSet<Integer>();
		int value = 0;
		int sum = 0;
		int count = 0;

		for(int i = (previousSeconds - 1); i > -1; i--) {
			//Logger.info("previousSeconds=" + (second - (i * 1000)));
			Iterator<Vertex> it = g.getVertices("second", second - (i * 1000)).iterator();
			Vertex v = null;

			if(it.hasNext()) {
				v = it.next();
				for(Vertex vertex : v.query().labels("include").has("event", target).vertices()) {
					if(vertex == null) {
						continue;
					}
					value = (Integer) vertex.getProperty("elapsedTime");
					count++;
					sum += value;
					numSet.add(value);
				}
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
				if(vertex == null) {
					continue;
				}
				value = (Integer) vertex.getProperty("elapsedTime");
				count++;
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
	}

	private static boolean getElapsedTimeDetail(long vertexId, ObjectNode objectNode) {
		boolean isSuccess = true;

		TitanGraph g = Global.getGraph();

		Vertex aVertex = g.getVertex(vertexId);
		if(aVertex == null) {
			Logger.info("The vertex does not exist: [" + vertexId + "]");
			isSuccess = false;
			return isSuccess;
		}
		String guuid = aVertex.getProperty("uuid");
		if(guuid == null) {
			Logger.info("The vertex has no uuid property");
			isSuccess = false;
			return isSuccess;
		}

		String eventName = null;
		String clientIp = null;
		String url = null;
		int browserResponseTime = 0;
		int serverResponseTime = 0;
		int controllerElapsedTime = 0;
		int daoElapsedTime = 0;
		int sqlElapsedTime = 0;

		Iterator<Vertex> it = g.getVertices("guuid", guuid).iterator();
		if(it.hasNext()) {
			Vertex guuidVertex = it.next();
			for(Vertex eventVertex : guuidVertex.query().labels("flow").vertices()) {
				if(eventVertex == null) {
					continue;
				}
				
				eventName = eventVertex.getProperty("event");
				//Logger.info("Event Name =======> " + eventName);
				//int depth = (Integer) eventVertex.getProperty("depth");
				if(eventName != null) {
					if("Request".equals(eventName)) {
						clientIp = eventVertex.getProperty("clientIp");
						url = eventVertex.getProperty("url");
					}
					if("Response".equals(eventName)) {
						browserResponseTime = (Integer) eventVertex.getProperty("elapsedTime");
					}
					if("EntryOperationReturn".equals(eventName)) {
						serverResponseTime += (Integer) eventVertex.getProperty("elapsedTime");
					}
					if("OperationReturn".equals(eventName)) {
						if(eventVertex.getProperty("depth") == 2) {
							controllerElapsedTime += (Integer) eventVertex.getProperty("elapsedTime"); 
						}
						if(eventVertex.getProperty("depth") == 3) {
							daoElapsedTime += (Integer) eventVertex.getProperty("elapsedTime"); 
						}
					}
					if("SqlReturn".equals(eventName)) {
						sqlElapsedTime += (Integer) eventVertex.getProperty("elapsedTime"); 
					}
				}
			}
		}
		objectNode.put("clientIp", clientIp);
		objectNode.put("url", url);
		objectNode.put("r0", browserResponseTime);
		objectNode.put("r1", serverResponseTime);
		objectNode.put("r2", controllerElapsedTime);
		objectNode.put("r3", daoElapsedTime);
		objectNode.put("r4", sqlElapsedTime);
		
		return isSuccess;
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
	//}
}
