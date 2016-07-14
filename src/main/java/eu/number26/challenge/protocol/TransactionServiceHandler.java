package eu.number26.challenge.protocol;

import io.netty.handler.codec.http.HttpMethod;

public class TransactionServiceHandler implements ProtocolHandler {

	private static final Link LINK = new Link(HttpMethod.GET, "/transactionservice");
	
	@Override
	public Link getLink() {
		return LINK;
	}

	@Override
	public String handle(String json) throws Exception {
		return null;
	}
}
