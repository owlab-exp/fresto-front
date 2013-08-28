package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.*;

import views.html.*;

import java.util.*;

public class Application extends Controller {
  
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
	    String uuid = data.get("uuid");
	    String targetUrl = data.get("targetUrl");
	    String timestamp = data.get("timestamp");

	    Logger.info(stage + "," + clientId + "," + uuid + "," + targetUrl + "," + timestamp);

	    return ok("RECV_OK");
    }
}
