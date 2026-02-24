package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String incentiveApiUrl = "http://localhost:8080/incentive";

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        // One single "if" to check if the transaction is possible
        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            
            // 1. Call the Incentive API first
            Incentive incentive = restTemplate.postForObject(incentiveApiUrl, transaction, Incentive.class);
            float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0f;

            // 2. Update the balances
            // Sender loses the amount
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            // Recipient gets amount PLUS the bonus from the API
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            // 3. Save the users (updates their balance in the DB)
            userRepository.save(sender);
            userRepository.save(recipient);

            // 4. Record the transaction with the incentive included
            transactionRepository.save(new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount));
        }
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }
}