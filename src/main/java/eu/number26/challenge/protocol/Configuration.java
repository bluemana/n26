package eu.number26.challenge.protocol;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.number26.challenge.protocol.sum.GetSumHandler;
import eu.number26.challenge.protocol.transaction.GetTransactionHandler;
import eu.number26.challenge.protocol.transaction.PutTransactionHandler;
import eu.number26.challenge.protocol.type.GetTypeHandler;

public class Configuration {

	private static final Map<Link, ProtocolHandler> HANDLERS = new HashMap<Link, ProtocolHandler>();
	public static final Gson GSON = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		.create();
	
	static {
		ProtocolHandler handler = new GetHandler();
		HANDLERS.put(handler.getLink(), handler);
		handler = new GetTransactionServiceHandler();
		HANDLERS.put(handler.getLink(), handler);
		handler = new PutTransactionHandler();
		HANDLERS.put(handler.getLink(), handler);
		handler = new GetTransactionHandler();
		HANDLERS.put(handler.getLink(), handler);
		handler = new GetTypeHandler();
		HANDLERS.put(handler.getLink(), handler);
		handler = new GetSumHandler();
		HANDLERS.put(handler.getLink(), handler);
	}
	
	public static ProtocolHandler getHandler(Link link) {
		return HANDLERS.get(link);
	}
}
