package eu.number26.challenge.protocol.transaction;

import java.math.BigDecimal;

public class PutTransactionRequest {

	private final Long parentId;
	private final String type;
	private final BigDecimal amount;
	
	public PutTransactionRequest(Long parentId, String type, BigDecimal amount) {
		this.parentId = parentId;
		this.type = type;
		this.amount = amount;
	}
	
	public Long getParentId() {
		return parentId;
	}
	
	public String getType() {
		return type;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
}
