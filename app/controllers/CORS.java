package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.*;

import views.html.*;

import java.util.*;

public class CORS extends Controller {

    public static Result checkCORS() {
	    response().setHeader("Access-Control-Allow-Origin", "*");
	    response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
	    response().setHeader("Access-Control-Max-Age", "300");
	    response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        return ok();
    }
}
