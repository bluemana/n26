package eu.number26.challenge.protocol;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private static final Map<Link, ProtocolHandler> HANDLERS = new HashMap<Link, ProtocolHandler>();
	
	static {
		ProtocolHandler handler = new BaseResourceHandler();
		HANDLERS.put(handler.getLink(), handler);
		handler = new TransactionServiceHandler();
		HANDLERS.put(handler.getLink(), handler);
	}
	
	public static ProtocolHandler getHandler(Link link) {
		return HANDLERS.get(link);
	}
}
