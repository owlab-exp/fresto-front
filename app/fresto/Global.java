package fresto;

import play.*;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class Global extends GlobalSettings {

	private String pubHost = "localhost";
	private int pubPort = 7000;
	private static ZMQ.Context context;
	private static ZMQ.Socket publisher;

	@Override
		public void onStart(Application app) {
			Logger.info("Connecting JeroMQ socket");
			
			context = ZMQ.context(1);
			publisher = context.socket(ZMQ.PUB);
			publisher.connect("tcp://" + pubHost + ":" + pubPort);

			Logger.info("JeroMQ publisher uses " + pubPort + " port.");
			Logger.info("Application has started");
		}

	@Override
		public void onStop(Application app) {
			Logger.info("Application shutdown...");
			Logger.info("Closing JeroMQ sockets");
			publisher.close();
			context.term();
		}

	public static ZMQ.Socket getPublisher(){
		// If sending is atomic action, then this method can be used
		return publisher;
	}

	public static synchronized void publishToMonitor(String topic, byte[] message){

		publisher.send(topic.getBytes(), ZMQ.SNDMORE);
		publisher.send(message, 0);

	}
}
