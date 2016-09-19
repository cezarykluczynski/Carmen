package com.cezarykluczynski.carmen.util.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.function.Function;

@Service
public class TransactionalExecutor {

    private PlatformTransactionManager platformTransactionManager;

    private EntityManager entityManager;

    @Autowired
    public TransactionalExecutor(PlatformTransactionManager platformTransactionManager, EntityManager entityManager) {
        this.platformTransactionManager = platformTransactionManager;
        this.entityManager = entityManager;
    }

    public void execute(Function<EntityManager, EntityManager> function) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                function.apply(entityManager);
            }
        });
    }

}