
package io.spring.presentations.jcmm;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

public class StupidPlatformTransactionManager implements PlatformTransactionManager {

	public TransactionStatus getTransaction(TransactionDefinition definition)
			throws TransactionException {
		return new SimpleTransactionStatus();
	}

	public void commit(TransactionStatus status) throws TransactionException {
		System.out.println("\n\nYay! Commit It\n\n");
	}

	public void rollback(TransactionStatus status) throws TransactionException {
		System.out.println("\n\nBooo, Roll it back :(\n\n");
	}

}
