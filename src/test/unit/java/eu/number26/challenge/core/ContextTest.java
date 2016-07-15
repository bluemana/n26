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
		Transaction t1 = new Transaction(1, null, "one", new BigDecimal(2));
		Transaction t2 = new Transaction(2, 1L, "two", new BigDecimal(3));
		Transaction t3 = new Transaction(3, null, "one", new BigDecimal(10.4));
		context.addTransaction(t1);
		context.addTransaction(t2);
		context.addTransaction(t3);
		Set<Transaction> expected = new HashSet<>();
		expected.add(t1);
		expected.add(t3);
		Set<Transaction> result = context.getTransactions("one");
		Assert.assertTrue(expected.equals(result));
	}
	
	@Test
	public void type_GetEmptyType_EmptySet() {
		Context context = new Context();
		Assert.assertTrue(context.getTransactions("car").isEmpty());
	}
}
