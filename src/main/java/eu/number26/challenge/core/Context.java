package eu.number26.challenge.core;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class Context {

	private static final Logger LOGGER = Logger.getLogger(Context.class);
	
	private class ContextTransaction {
		
		private final Transaction transaction;
		private BigDecimal transitiveSum;
		
		public ContextTransaction(Transaction transaction) {
			this.transaction = transaction;
			transitiveSum = transaction.getAmount();
		}
		
		public Transaction getTransaction() {
			return transaction;
		}
		
		public BigDecimal getTransitiveSum() {
			return transitiveSum;
		}
		
		public void setTransitiveSum(BigDecimal transitiveSum) {
			this.transitiveSum = transitiveSum;
		}
	}
	
	private final ExecutorService executor;
	private final Map<Long, ContextTransaction> transactionsById;
	private final Map<String, Set<ContextTransaction>> transactionsByType;
	
	public Context() {
		executor = Executors.newSingleThreadExecutor();
		transactionsById = new HashMap<>();
		transactionsByType = new HashMap<>();
	}
	
	public void addTransaction(Transaction transaction) {
		try {
			executor.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					if (!transactionsById.containsKey(transaction.getId())) {
						if (transaction.getParentId() == null ||
								transactionsById.containsKey(transaction.getParentId())) {
							ContextTransaction contextTransaction = new ContextTransaction(transaction);
							transactionsById.put(transaction.getId(), contextTransaction);
							Set<ContextTransaction> contextTransactions = transactionsByType.get(transaction.getType());
							if (contextTransactions == null) {
								contextTransactions = new HashSet<>();
								transactionsByType.put(transaction.getType(), contextTransactions);
							}
							contextTransactions.add(new ContextTransaction(transaction));
							updateTransitiveSums(contextTransaction);
						} else {
							throw new IllegalArgumentException("Invalid parent ID: " + transaction.getParentId());
						}
					} else {
						throw new IllegalArgumentException("Invalid transaction ID: " + transaction.getId());
					}
					return null;
				}
				
			}).get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	private void updateTransitiveSums(ContextTransaction contextTransaction) {
		ContextTransaction current = contextTransaction;
		while (current.getTransaction().getParentId() != null) {
			ContextTransaction parent = transactionsById.get(current.getTransaction().getParentId());
			parent.setTransitiveSum(parent.getTransitiveSum().add(contextTransaction.getTransitiveSum()));
			current = parent;
		}
	}
	
	public Transaction getTransaction(long id) {
		Transaction result = null;
		try {
			result = executor.submit(new Callable<Transaction>() {

				@Override
				public Transaction call() throws Exception {
					ContextTransaction contextTransaction = transactionsById.get(id);
					return contextTransaction != null ? contextTransaction.getTransaction() : null;
				}
				
			}).get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}
	
	public Set<Transaction> getTransactions(String type) {
		Set<Transaction> result = null;
		try {
			result = executor.submit(new Callable<Set<Transaction>>() {

				@Override
				public Set<Transaction> call() throws Exception {
					Set<ContextTransaction> contextTransactions = transactionsByType.get(type);
					if (contextTransactions != null) {
						Set<Transaction> transactionsSet = new HashSet<>();
						for (ContextTransaction contextTransaction : contextTransactions) {
							transactionsSet.add(contextTransaction.getTransaction());
						}
						return transactionsSet;
					} else {
						return Collections.emptySet();
					}
				}
				
			}).get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}
	
	public BigDecimal transitiveSum(long transactionId) {
		BigDecimal result = null;
		try {
			result = executor.submit(new Callable<BigDecimal>() {

				@Override
				public BigDecimal call() throws Exception {
					ContextTransaction contextTransaction = transactionsById.get(transactionId);
					if (contextTransaction != null) {
						return contextTransaction.getTransitiveSum();
					} else {
						return null;
					}
				}
			}).get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}
}
