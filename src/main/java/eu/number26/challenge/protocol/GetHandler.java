package eu.number26.challenge.protocol;

import eu.number26.challenge.core.Context;
import io.netty.handler.codec.http.HttpMethod;

public class GetHandler implements ProtocolHandler {

	private static final Link LINK = new Link(HttpMethod.GET, "");
	
	@Override
	public Link getLink() {
		return LINK;
	}

	@Override
	public String handle(Context context, Link link, String body) throws Exception {
		return null;
	}
}
