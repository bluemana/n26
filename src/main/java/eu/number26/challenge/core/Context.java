package eu.number26.challenge.core;

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
	
	private final ExecutorService executor;
	private final Map<Long, Transaction> transactionsById;
	private final Map<String, Set<Transaction>> transactionsByType;
	
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
						transactionsById.put(transaction.getId(), transaction);
						Set<Transaction> transactions = transactionsByType.get(transaction.getType());
						if (transactions == null) {
							transactions = new HashSet<>();
							transactionsByType.put(transaction.getType(), transactions);
						}
						transactions.add(transaction);
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
	
	public Transaction getTransaction(long id) {
		Transaction result = null;
		try {
			result = executor.submit(new Callable<Transaction>() {

				@Override
				public Transaction call() throws Exception {
					return transactionsById.get(id);
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
					Set<Transaction> transactions = transactionsByType.get(type);
					if (transactions != null) {
						return new HashSet<>(transactions);
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
}
