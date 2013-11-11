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

	private String pubHost = "fresto1";
	private int pubPort = 7000;
	private static ZMQ.Context context;
	private static ZMQ.Socket publisher;
	private static TitanGraph g;

	@Override
		public void onStart(Application app) {
			Logger.info("Connecting JeroMQ socket");
			
			context = ZMQ.context(1);
			publisher = context.socket(ZMQ.PUB);
			publisher.connect("tcp://" + pubHost + ":" + pubPort);

			Logger.info("JeroMQ publisher uses " + pubPort + " port.");

		}

	@Override
		public void onStop(Application app) {
			Logger.info("Application shutdown...");
			Logger.info("Closing JeroMQ sockets");
			publisher.close();
			context.term();

			if(g.isOpen()) {
				g.shutdown();
			}
		}

	public static ZMQ.Socket getPublisher(){
		// If sending is atomic action, then this method can be used
		return publisher;
	}

	public static synchronized void publishToMonitor(String topic, byte[] message){

		publisher.send(topic.getBytes(), ZMQ.SNDMORE);
		publisher.send(message, 0);

	}

	public static TitanGraph openGraph() { 
		Configuration conf = new BaseConfiguration(); 
		conf.setProperty("storage.backend", "cassandra"); 
		conf.setProperty("storage.hostname", "fresto2.owlab.com"); 
		conf.setProperty("storage.connection-pool-size", 64); 
		g = TitanFactory.open(conf); 
		return g;
	}

	public static TitanGraph getGraph() {
		if(g == null || !g.isOpen()) {
			openGraph();
		}
		return g;
	}
}
