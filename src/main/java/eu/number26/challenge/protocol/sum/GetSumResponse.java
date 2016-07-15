package eu.number26.challenge.protocol.sum;

import java.math.BigDecimal;

public class GetSumResponse {

	private final BigDecimal sum;
	
	public GetSumResponse(BigDecimal sum) {
		this.sum = sum;
	}
	
	public BigDecimal getSum() {
		return sum;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof GetSumResponse) {
			result = ((GetSumResponse) obj).sum.equals(sum);
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return sum.hashCode();
	}
}
