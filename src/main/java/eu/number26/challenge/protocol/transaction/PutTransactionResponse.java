package eu.number26.challenge.protocol.transaction;

public class PutTransactionResponse {

	public enum Status {
		OK;
		
		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
	
	private final Status status;
	
	public PutTransactionResponse(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PutTransactionResponse && ((PutTransactionResponse) obj).status.equals(status);
	}
	
	@Override
	public int hashCode() {
		return status.hashCode();
	}
}
