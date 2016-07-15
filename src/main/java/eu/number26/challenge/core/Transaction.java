package eu.number26.challenge.core;

import java.math.BigDecimal;

public class Transaction {

	private final long id;
	private final Long parentId;
	private final String type;
	private final BigDecimal amount;
	
	public Transaction(long id, Long parentId, String type, BigDecimal amount) {
		this.id = id;
		this.parentId = parentId;
		this.type = type;
		this.amount = amount;
	}
	
	public long getId() {
		return id;
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
		if (obj instanceof Transaction) {
			Transaction t = (Transaction) obj;
			result = t.id == id;
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}
}
