package eu.number26.challenge.protocol.sum;

import java.math.BigDecimal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.number26.challenge.core.Context;
import eu.number26.challenge.protocol.Link;
import eu.number26.challenge.protocol.ProtocolHandler;
import io.netty.handler.codec.http.HttpMethod;

public class GetSumHandler implements ProtocolHandler {

	private static final Link LINK = new Link(HttpMethod.GET, "/transactionservice/sum");
	private static final Gson GSON = new GsonBuilder().create();
	
	@Override
	public Link getLink() {
		return LINK;
	}

	@Override
	public String handle(Context context, Link link, String body) throws Exception {
		BigDecimal sum = context.transitiveSum(Long.parseLong(link.getResourceId()));
		if (sum != null) {
			return GSON.toJson(new GetSumResponse(sum));
		} else {
			throw new IllegalArgumentException("Invalid transaction ID: " + link.getResourceId());
		}
	}
}
