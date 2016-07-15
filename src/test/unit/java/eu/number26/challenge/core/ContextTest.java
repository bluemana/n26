package eu.number26.challenge.core;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class ContextTest {

	@Test
	public void type_GetType_Returned() {
		Context context = new Context();
		Transaction t1 = new Transaction(1, null, "car", new BigDecimal(2));
		Transaction t2 = new Transaction(2, 1L, "kids", new BigDecimal(3));
		Transaction t3 = new Transaction(3, null, "car", new BigDecimal(10.4));
		context.addTransaction(t1);
		context.addTransaction(t2);
		context.addTransaction(t3);
		Set<Transaction> expected = new HashSet<>();
		expected.add(t1);
		expected.add(t3);
		Set<Transaction> result = context.getTransactions("car");
		Assert.assertTrue(expected.equals(result));
	}
	
	@Test
	public void type_GetEmptyType_EmptySet() {
		Context context = new Context();
		Assert.assertTrue(context.getTransactions("car").isEmpty());
	}
	
	@Test
	public void sum_LeafTransaction_TransactionAmount() {
		Context context = new Context();
		context.addTransaction(new Transaction(10, null, "cars", new BigDecimal(5000)));
		context.addTransaction(new Transaction(11, 10L, "shopping", new BigDecimal(10000)));
		context.addTransaction(new Transaction(1, null, "theatre", new BigDecimal(120.5)));
		BigDecimal expected = new BigDecimal(10000);
		BigDecimal result = context.transitiveSum(11);
		Assert.assertTrue(expected.equals(result));
		expected = new BigDecimal(120.5);
		result = context.transitiveSum(1);
		Assert.assertTrue(expected.equals(result));
	}
	
	@Test
	public void sum_TransactionWithChildren_Calculated() {
		Context context = new Context();
		context.addTransaction(new Transaction(10, null, "cars", new BigDecimal(5000)));
		context.addTransaction(new Transaction(11, 10L, "shopping", new BigDecimal(10000)));
		context.addTransaction(new Transaction(1, null, "theatre", new BigDecimal(120.5)));
		context.addTransaction(new Transaction(2, 1L, "kids", new BigDecimal(1000)));
		context.addTransaction(new Transaction(3, 1L, "university", new BigDecimal(250000)));
		context.addTransaction(new Transaction(4, 3L, "entertainment", new BigDecimal(500)));
		BigDecimal expected = new BigDecimal(15000);
		BigDecimal result = context.transitiveSum(10);
		Assert.assertTrue(expected.equals(result));
		expected = new BigDecimal(251620.5);
		result = context.transitiveSum(1);
		Assert.assertTrue(expected.equals(result));
		expected = new BigDecimal(250500);
		result = context.transitiveSum(3);
		Assert.assertTrue(expected.equals(result));
	}
}
