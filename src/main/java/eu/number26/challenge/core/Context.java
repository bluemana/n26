package eu.number26.challenge.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class Context {

	private static final Logger LOGGER = Logger.getLogger(Context.class);
	
	private final ExecutorService executor;
	private final Map<Long, Transaction> transactions;
	
	public Context() {
		executor = Executors.newSingleThreadExecutor();
		transactions = new HashMap<>();
	}
	
	public void addTransaction(Transaction transaction) {
		try {
			executor.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					if (!transactions.containsKey(transaction.getId())) {
						transactions.put(transaction.getId(), transaction);
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
}
