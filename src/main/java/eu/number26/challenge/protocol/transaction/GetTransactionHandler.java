package eu.number26.challenge.protocol.transaction;

import com.google.gson.Gson;

import eu.number26.challenge.core.Context;
import eu.number26.challenge.core.Transaction;
import eu.number26.challenge.protocol.Configuration;
import eu.number26.challenge.protocol.Link;
import eu.number26.challenge.protocol.ProtocolHandler;
import io.netty.handler.codec.http.HttpMethod;

public class GetTransactionHandler implements ProtocolHandler {

	private static final Link LINK = new Link(HttpMethod.GET, "/transactionservice/transaction");
	private static final Gson GSON = Configuration.GSON;
	
	@Override
	public Link getLink() {
		return LINK;
	}

	@Override
	public String handle(Context context, Link link, String body) throws Exception {
		try {
			long transactionId = Long.parseLong(link.getResourceId());
			Transaction transaction = context.getTransaction(transactionId);
			if (transaction != null) {
				GetTransactionResponse response = new GetTransactionResponse(transaction.getParentId(),
						transaction.getType(), transaction.getAmount());
				return GSON.toJson(response);
			} else {
				throw new IllegalArgumentException("Transaction not found (ID: " + transactionId + ")");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid transaction ID: " + link.getResourceId(), e);
		}
	}
}
