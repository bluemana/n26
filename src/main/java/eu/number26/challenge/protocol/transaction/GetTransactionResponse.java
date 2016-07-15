package eu.number26.challenge.protocol.transaction;

import java.math.BigDecimal;
import java.util.Objects;

public class GetTransactionResponse {

	private final Long parentId;
	private final String type;
	private final BigDecimal amount;
	
	public GetTransactionResponse(Long parentId, String type, BigDecimal amount) {
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
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof GetTransactionResponse) {
			GetTransactionResponse response = (GetTransactionResponse) obj;
			result = Objects.equals(response.parentId, parentId) && response.type.equals(type) && response.amount.equals(amount);
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(parentId, type, amount);
	}
}
