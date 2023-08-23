package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.CREDIT;
import static com.mindhub.homebanking.models.CardType.DEBIT;


@SpringBootApplication

public class HomebankingApplication {

	private LocalDateTime localDateTime = LocalDateTime.now();
	private LocalDateTime localDateTimeNextDay = localDateTime.plusDays(1) ;
	private LocalDateTime localDateTimeTwoDays = localDateTime.plusDays(2) ;
	private LocalDateTime localDateTimeThreeDays = localDateTime.plusDays(3) ;

	private LocalDate cardEmissionDate = LocalDate.now();
	private LocalDate cardExpiryDate = cardEmissionDate.plusYears(5);

	private List<Integer> payments1 = Arrays.asList(12,24,36,48,60);
	private List<Integer> payments2 = Arrays.asList(6,12,24);
	private List<Integer> payments3 = Arrays.asList(6,12,24,36);

	@Autowired
	private PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository repositoryAccount, TransactionRepository repositoryTransaction, LoanRepository repositoryLoan, ClientLoanRepository repositoryClientLoan, CardRepository repositoryCard) {
		return (args) -> {
			Account firstAccount = new Account("VIN001", this.localDateTime, 5000);
			Account secondAccount = new Account("VIN002", this.localDateTimeNextDay, 7500);
			Account thirdAccount = new Account("VIN003", this.localDateTimeTwoDays, 8500);
			Account fourthAccount = new Account("VIN004", this.localDateTimeThreeDays, 900);
			Client melbaMorel = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("AlbaGuBrath"));
			Client darylBaptie = new Client("Daryl", "Baptie", "darylBaptie@gmail.com", passwordEncoder.encode("Hibs1875"));
			Client adminAdmin = new Client("Admin", "admin", "admin@mindhub.com", passwordEncoder.encode("admin2006"));
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, this.localDateTime, 3000, "salary"  );
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, this.localDateTime, -80, "supermarket"  );
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, this.localDateTime, -25, "Energy payment"  );
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, this.localDateTime, 2250, "salary"  );
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, this.localDateTime, -35.75, "Gas Station"  );
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, this.localDateTime, -125, "Student loan"  );
			Loan mortgage = new Loan("Mortgage", 500000, this.payments1);
			Loan personal = new Loan("Personal", 100000, this.payments2);
			Loan car = new Loan("Car", 300000, this.payments3);
			ClientLoan clientLoanMortgage = new ClientLoan(400000, 60);
			ClientLoan clientLoanPersonal = new ClientLoan(50000, 12);
			ClientLoan clientLoanPersonal2 = new ClientLoan(100000, 24);
			ClientLoan clientLoanCar = new ClientLoan(200000, 36);
			repositoryLoan.save(mortgage);
			repositoryLoan.save(personal);
			repositoryLoan.save(car);
			repositoryClient.save(melbaMorel);
			repositoryClient.save(darylBaptie);
			repositoryClient.save(adminAdmin);
			melbaMorel.addClientLoan(clientLoanMortgage);
			mortgage.addClientLoan(clientLoanMortgage);
			melbaMorel.addClientLoan(clientLoanPersonal);
			personal.addClientLoan(clientLoanPersonal);
			darylBaptie.addClientLoan(clientLoanPersonal2);
			personal.addClientLoan(clientLoanPersonal2);
			darylBaptie.addClientLoan(clientLoanCar);
			car.addClientLoan(clientLoanCar);
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
			repositoryClientLoan.save(clientLoanMortgage);
			repositoryClientLoan.save(clientLoanPersonal);
			repositoryClientLoan.save(clientLoanPersonal2);
			repositoryClientLoan.save(clientLoanCar);

			Card card1 = new Card(melbaMorel.getFirstName() + " " + melbaMorel.getLastName(), DEBIT, GOLD, "4582 8954 3594 3458", (short) 567, this.cardEmissionDate, this.cardExpiryDate);
			melbaMorel.addCard(card1);
			repositoryCard.save(card1);

			Card card2 = new Card(melbaMorel.getFirstName() + " " + melbaMorel.getLastName(), CREDIT, TITANIUM, "7685 9204 9384 4637", (short) 283, this.cardEmissionDate, this.cardExpiryDate);
			melbaMorel.addCard(card2);
			repositoryCard.save(card2);

			Card card3 = new Card(darylBaptie.getFirstName() + " " + darylBaptie.getLastName(), CREDIT, SILVER, "2839 5349 9384 1024", (short) 749, this.cardEmissionDate, this.cardExpiryDate);
			darylBaptie.addCard(card3);
			repositoryCard.save(card3);

			Card card4 = new Card(melbaMorel.getFirstName() + " " + melbaMorel.getLastName(), DEBIT, SILVER, "5849 6940 8235 1467", (short) 478, this.cardEmissionDate, this.cardExpiryDate);
			melbaMorel.addCard(card4);
			repositoryCard.save(card4);

			Card card5 = new Card(melbaMorel.getFirstName() + " " + melbaMorel.getLastName(), CREDIT, GOLD, "9712 5938 1242 7483", (short) 693, this.cardEmissionDate, this.cardExpiryDate);
			melbaMorel.addCard(card5);
			repositoryCard.save(card5);
			passwordEncoder.encode(melbaMorel.getPassword());
			passwordEncoder.encode(darylBaptie.getPassword());

		};

	}








}
