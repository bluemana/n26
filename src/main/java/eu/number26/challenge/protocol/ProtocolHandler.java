package eu.number26.challenge.protocol;

public interface ProtocolHandler {

	public Link getLink();
	
	public String handle(String json) throws Exception;
}
