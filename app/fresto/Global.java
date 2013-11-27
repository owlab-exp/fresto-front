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
package fresto;

import play.*;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;

public class Global extends GlobalSettings {

	//private String pubHost = "fresto1";
	//private int pubPort = 7000;
	//private static ZMQ.Context context;
	//private static ZMQ.Socket publisher;
	private static String TITAN_STORAGE_BACKEND;
	private static String TITAN_STORAGE_HOSTNAME;
	private static TitanGraph g;

	@Override
		public void onStart(Application app) {
			//Logger.info("Connecting JeroMQ socket");
			//
			//context = ZMQ.context(1);
			//publisher = context.socket(ZMQ.PUB);
			//publisher.connect("tcp://" + pubHost + ":" + pubPort);

			//Logger.info("JeroMQ publisher uses " + pubPort + " port.");

			TITAN_STORAGE_BACKEND = Play.application().configuration().getString("titan.storage.backend");
			TITAN_STORAGE_HOSTNAME = Play.application().configuration().getString("titan.storage.hostname");

		}

	@Override
		public void onStop(Application app) {
			//Logger.info("Application shutdown...");
			//Logger.info("Closing JeroMQ sockets");
			//publisher.close();
			//context.term();

			if(g != null && g.isOpen()) {
				g.shutdown();
			}
		}

	//public static ZMQ.Socket getPublisher(){
	//	// If sending is atomic action, then this method can be used
	//	return publisher;
	//}

	//public static synchronized void publishToMonitor(String topic, byte[] message){

	//	publisher.send(topic.getBytes(), ZMQ.SNDMORE);
	//	publisher.send(message, 0);

	//}

	public static TitanGraph openGraph() { 
		Configuration conf = new BaseConfiguration(); 
		//conf.setProperty("storage.backend", "cassandra"); 
		conf.setProperty("storage.backend", TITAN_STORAGE_BACKEND); 
		conf.setProperty("storage.hostname", TITAN_STORAGE_HOSTNAME); 
		// Default
		//conf.setProperty("storage.connection-pool-size", 32); 
		g = TitanFactory.open(conf); 
		return g;
	}

	public static synchronized TitanGraph getGraph() {
		if(g == null || !g.isOpen()) {
			openGraph();
		}
		return g;
	}
}
