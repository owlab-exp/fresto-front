package fresto;

import play.*;
import play.mvc.*;
import play.mvc.Http.Context;
import play.mvc.Http.Response;

public class CORSAction extends Action.Simple {
	public Result call(Context context) throws Throwable {
		Response response = context.response();
		response.setHeader("Access-Control-Allow-Origin", "*");

		// Handle preflight requests
		if(context.request().method().equals("OPTIONS")) {
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Auth-Tocken");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			return ok();
		}

		response.setHeader("Access-Control-Allow-Headers","X-Requested-With, Content-Type, X-Auth-Token");
		return delegate.call(context);
	}
}
