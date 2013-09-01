// @SOURCE:/home/ernest/Dev/fresto-front/conf/routes
// @HASH:62a8dd159e35046268964fed067885894f79ad1d
// @DATE:Sun Sep 01 23:38:46 KST 2013


import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString

object Routes extends Router.Routes {

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix  
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" } 


// @LINE:6
private[this] lazy val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
        

// @LINE:7
private[this] lazy val controllers_Application_whatIsMyIPAddress1 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("whatIsMyIPAddress"))))
        

// @LINE:9
private[this] lazy val controllers_Application_feedUIEvent2 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("feedUIEvent"))))
        

// @LINE:10
private[this] lazy val controllers_SSE_r0Stream3 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("r0Stream"))))
        

// @LINE:13
private[this] lazy val controllers_Assets_at4 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
        

// @LINE:16
private[this] lazy val controllers_ClockTutorial_liveClock5 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("clock"))))
        
def documentation = List(("""GET""", prefix,"""controllers.Application.index()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """whatIsMyIPAddress""","""controllers.Application.whatIsMyIPAddress()"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """feedUIEvent""","""controllers.Application.feedUIEvent()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """r0Stream""","""controllers.SSE.r0Stream()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """clock""","""controllers.ClockTutorial.liveClock()""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]] 
}}
       
    
def routes:PartialFunction[RequestHeader,Handler] = {        

// @LINE:6
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
   }
}
        

// @LINE:7
case controllers_Application_whatIsMyIPAddress1(params) => {
   call { 
        invokeHandler(controllers.Application.whatIsMyIPAddress(), HandlerDef(this, "controllers.Application", "whatIsMyIPAddress", Nil,"GET", """""", Routes.prefix + """whatIsMyIPAddress"""))
   }
}
        

// @LINE:9
case controllers_Application_feedUIEvent2(params) => {
   call { 
        invokeHandler(controllers.Application.feedUIEvent(), HandlerDef(this, "controllers.Application", "feedUIEvent", Nil,"POST", """GET	/feedUIEvent		    controllers.Application.feedUIEvent()""", Routes.prefix + """feedUIEvent"""))
   }
}
        

// @LINE:10
case controllers_SSE_r0Stream3(params) => {
   call { 
        invokeHandler(controllers.SSE.r0Stream(), HandlerDef(this, "controllers.SSE", "r0Stream", Nil,"GET", """""", Routes.prefix + """r0Stream"""))
   }
}
        

// @LINE:13
case controllers_Assets_at4(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
   }
}
        

// @LINE:16
case controllers_ClockTutorial_liveClock5(params) => {
   call { 
        invokeHandler(controllers.ClockTutorial.liveClock(), HandlerDef(this, "controllers.ClockTutorial", "liveClock", Nil,"GET", """ For date streaming test""", Routes.prefix + """clock"""))
   }
}
        
}
    
}
        