// @SOURCE:/working/devel/fresto-front/conf/routes
// @HASH:7841090ceec49e18d4ff88396a650d5867c94a1e
// @DATE:Tue Aug 27 05:19:03 KST 2013

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString


// @LINE:12
// @LINE:9
// @LINE:7
// @LINE:6
package controllers {

// @LINE:9
// @LINE:7
// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index(): Call = {
   Call("GET", _prefix)
}
                                                

// @LINE:9
def feedUIEvent(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "feedUIEvent")
}
                                                

// @LINE:7
def whatIsMyIPAddress(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "whatIsMyIPAddress")
}
                                                
    
}
                          

// @LINE:12
class ReverseAssets {
    

// @LINE:12
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          
}
                  


// @LINE:12
// @LINE:9
// @LINE:7
// @LINE:6
package controllers.javascript {

// @LINE:9
// @LINE:7
// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        

// @LINE:9
def feedUIEvent : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.feedUIEvent",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "feedUIEvent"})
      }
   """
)
                        

// @LINE:7
def whatIsMyIPAddress : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.whatIsMyIPAddress",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "whatIsMyIPAddress"})
      }
   """
)
                        
    
}
              

// @LINE:12
class ReverseAssets {
    

// @LINE:12
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              
}
        


// @LINE:12
// @LINE:9
// @LINE:7
// @LINE:6
package controllers.ref {

// @LINE:9
// @LINE:7
// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

// @LINE:9
def feedUIEvent(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.feedUIEvent(), HandlerDef(this, "controllers.Application", "feedUIEvent", Seq(), "POST", """GET	/feedUIEvent		    controllers.Application.feedUIEvent()""", _prefix + """feedUIEvent""")
)
                      

// @LINE:7
def whatIsMyIPAddress(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.whatIsMyIPAddress(), HandlerDef(this, "controllers.Application", "whatIsMyIPAddress", Seq(), "GET", """""", _prefix + """whatIsMyIPAddress""")
)
                      
    
}
                          

// @LINE:12
class ReverseAssets {
    

// @LINE:12
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          
}
                  
      