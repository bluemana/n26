package eu.number26.challenge.protocol;

import java.util.Objects;

import io.netty.handler.codec.http.HttpMethod;

public class Link {

	private final HttpMethod method;
	private final String uri;
	
	public Link(HttpMethod method, String uri) {
		this.method = method;
		this.uri = uri;
	}
	
	public HttpMethod getMethod() {
		return method;
	}
	
	public String getUri() {
		return uri;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Link) {
			Link link = (Link) obj;
			result = link.method.equals(method) && link.uri.equals(uri);
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(method.name(), uri);
	}
}
