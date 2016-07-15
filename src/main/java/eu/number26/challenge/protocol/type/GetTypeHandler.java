package eu.number26.challenge.protocol.type;

import java.util.Set;

import com.google.gson.Gson;

import eu.number26.challenge.core.Context;
import eu.number26.challenge.core.Transaction;
import eu.number26.challenge.protocol.Configuration;
import eu.number26.challenge.protocol.Link;
import eu.number26.challenge.protocol.ProtocolHandler;
import io.netty.handler.codec.http.HttpMethod;

public class GetTypeHandler implements ProtocolHandler {

	private static final Link LINK = new Link(HttpMethod.GET, "/transactionservice/type");
	private static final Gson GSON = Configuration.GSON;
	
	@Override
	public Link getLink() {
		return LINK;
	}

	@Override
	public String handle(Context context, Link link, String body) throws Exception {
		Set<Transaction> transactions = context.getTransactions(link.getResourceId());
		GetTypeResponse response = new GetTypeResponse();
		for (Transaction transaction : transactions) {
			response.add(transaction.getId());
		}
		return GSON.toJson(response);
	}
}
