/**************************************************************************************
 * Copyright 2013 TheSystemIdeas, Inc and Contributors. All rights reserved.          *
 *                                                                                    *
 *     https://github.com/owlab/fresto                                                *
 *                                                                                    *
 *                                                                                    *
 * ---------------------------------------------------------------------------------- *
 * This file is licensed under the Apache License, Version 2.0 (the "License");       *
 * you may not use this file except in compliance with the License.                   *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 * 
 **************************************************************************************/
package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.*;

import views.html.*;

import java.util.*;

import org.apache.thrift.TSerializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;

import fresto.data.ClientID;
import fresto.data.ResourceID;
import fresto.data.RequestEdge;
import fresto.data.ResponseEdge;
import fresto.data.DataUnit;
import fresto.data.Pedigree;
import fresto.data.FrestoData;

import fresto.Global;

public class Application extends Controller {
 	//private static String pubHost = "localhost";
	//private static int pubPort = 7000;

    public static Result index() {
        return ok();
    }

  
}
