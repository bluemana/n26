package eu.number26.challenge.protocol.transaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.number26.challenge.core.Context;
import eu.number26.challenge.core.Transaction;
import eu.number26.challenge.protocol.Link;
import eu.number26.challenge.protocol.ProtocolHandler;
import io.netty.handler.codec.http.HttpMethod;

public class PutTransactionHandler implements ProtocolHandler {

	private static final Link LINK = new Link(HttpMethod.PUT, "/transactionservice/transaction");
	private static final Gson GSON = new GsonBuilder().create();
	
	@Override
	public Link getLink() {
		return LINK;
	}

	@Override
	public String handle(Context context, Link link, String body) throws Exception {
		PutTransactionRequest request = GSON.fromJson(body, PutTransactionRequest.class);
		context.addTransaction(new Transaction(Long.parseLong(link.getResourceId()),
			request.getParentId(), request.getType(), request.getAmount()));
		return GSON.toJson(new PutTransactionResponse(PutTransactionResponse.Status.OK));
	}
}
