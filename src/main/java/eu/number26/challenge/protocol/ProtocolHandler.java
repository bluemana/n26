package eu.number26.challenge.protocol;

import eu.number26.challenge.core.Context;

public interface ProtocolHandler {

	public Link getLink();
	
	public String handle(Context context, Link link, String body) throws Exception;
}
