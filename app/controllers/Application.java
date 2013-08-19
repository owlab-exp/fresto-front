package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import java.util.*;

public class Application extends Controller {
  
    public static Result index() {
        //return ok(index.render("Your new application is ready."));
	StringBuffer sb = new StringBuffer();
	sb.append("Use the following urls to give events and get events");
	sb.append("\n");
	sb.append("http://fresto1.owlab.com:9999/feedR0");
	sb.append("\n");
	sb.append("http://fresto1.owlab.com:9999/getR0");
        return ok(sb.toString());
    }
  
}
