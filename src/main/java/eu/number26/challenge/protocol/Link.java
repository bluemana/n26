package eu.number26.challenge.protocol;

import io.netty.handler.codec.http.HttpMethod;

public class Link {
	
	private static final String BASE_URI = "/transactionservice";

	private final String id;
	private final HttpMethod method;
	private final String uri;
	private final String resourceId;
	
	public Link(HttpMethod method, String uri) {
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.length() - 1);
		}
		if (uri.startsWith(BASE_URI)) {
			int idx = uri.indexOf('/', BASE_URI.length() + 1);
			if (idx > 0) {
				id = method.name() + " " + uri.substring(0, idx);
				resourceId = uri.substring(idx + 1, uri.length());
			} else {
				id = method.name() + " " + uri;
				resourceId = null;
			}
		} else {
			id = method.name() + " " + uri;
			resourceId = null;
		}
		this.method = method;
		this.uri = uri;
	}
	
	public String getId() {
		return id;
	}
	
	public HttpMethod getMethod() {
		return method;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getResourceId() {
		return resourceId;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Link && ((Link) obj).id.equals(id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
