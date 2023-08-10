package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDateTime;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;


@SpringBootApplication

public class HomebankingApplication {

	private LocalDateTime localDateTime = LocalDateTime.now();
	private LocalDateTime localDateTimeNextDay = localDateTime.plusDays(1) ;
	private LocalDateTime localDateTimeTwoDays = localDateTime.plusDays(2) ;
	private LocalDateTime localDateTimeThreeDays = localDateTime.plusDays(3) ;



	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository repositoryAccount, TransactionRepository repositoryTransaction) {
		return (args) -> {
			Account firstAccount = new Account("VIN001", this.localDateTime, 5000);
			Account secondAccount = new Account("VIN002", this.localDateTimeNextDay, 7500);
			Account thirdAccount = new Account("VIN003", this.localDateTimeTwoDays, 8500);
			Account fourthAccount = new Account("VIN004", this.localDateTimeThreeDays, 900);
			Client melbaMorel = new Client("Melba", "Morel", "melba@mindhub.com");
			Client darylBaptie = new Client("Daryl", "Baptie", "darylBaptie@gmail.com");
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, this.localDateTime, 3000, "salary"  );
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, this.localDateTime, -80, "supermarket"  );
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, this.localDateTime, -25, "Energy payment"  );
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, this.localDateTime, 2250, "salary"  );
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, this.localDateTime, -35.75, "Gas Station"  );
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, this.localDateTime, -125, "Student loan"  );
			repositoryClient.save(melbaMorel);
			repositoryClient.save(darylBaptie);
			melbaMorel.addAccount(firstAccount);
			melbaMorel.addAccount(secondAccount);
			darylBaptie.addAccount(thirdAccount);
			darylBaptie.addAccount(fourthAccount);
			repositoryAccount.save(firstAccount);
			repositoryAccount.save(secondAccount);
			repositoryAccount.save(thirdAccount);
			repositoryAccount.save(fourthAccount);
			firstAccount.addTransaction(transaction1);
			firstAccount.addTransaction(transaction2);
			firstAccount.addTransaction(transaction3);
			secondAccount.addTransaction(transaction4);
			secondAccount.addTransaction(transaction5);
			secondAccount.addTransaction(transaction6);
			repositoryTransaction.save(transaction1);
			repositoryTransaction.save(transaction2);
			repositoryTransaction.save(transaction3);
			repositoryTransaction.save(transaction4);
			repositoryTransaction.save(transaction5);
			repositoryTransaction.save(transaction6);
		};
	}








}
