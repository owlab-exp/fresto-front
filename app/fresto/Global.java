package fresto;

import play.*;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

public class Global extends GlobalSettings {

	private String pubHost = "fresto1";
	private int pubPort = 7000;
	private static ZMQ.Context context;
	private static ZMQ.Socket publisher;
	private static OGraphDatabase oGraph;

	@Override
		public void onStart(Application app) {
			Logger.info("Connecting JeroMQ socket");
			
			context = ZMQ.context(1);
			publisher = context.socket(ZMQ.PUB);
			publisher.connect("tcp://" + pubHost + ":" + pubPort);

			Logger.info("JeroMQ publisher uses " + pubPort + " port.");

			oGraph = new OGraphDatabase("remote:fresto3.owlab.com/frestodb");
			oGraph.setProperty("minPool", 3);
			oGraph.setProperty("maxPool", 10);

			Logger.info("Application has started");
		}

	@Override
		public void onStop(Application app) {
			Logger.info("Application shutdown...");
			Logger.info("Closing JeroMQ sockets");
			publisher.close();
			context.term();

			oGraph.close();
		}

	public static ZMQ.Socket getPublisher(){
		// If sending is atomic action, then this method can be used
		return publisher;
	}

	public static synchronized void publishToMonitor(String topic, byte[] message){

		publisher.send(topic.getBytes(), ZMQ.SNDMORE);
		publisher.send(message, 0);

	}

	public static OGraphDatabase openDatabase() {
		return oGraph.open("admin", "admin");
	}
}
