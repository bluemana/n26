package eu.number26.challenge.protocol.type;

import java.util.Collection;
import java.util.HashSet;

public class GetTypeResponse extends HashSet<Long> {

	private static final long serialVersionUID = 1L;
	
	public GetTypeResponse() {
	}

	public GetTypeResponse(Collection<? extends Long> c) {
		super(c);
	}
	
	public GetTypeResponse(int initialCapacity) {
		super(initialCapacity);
	}

	public GetTypeResponse(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
}
