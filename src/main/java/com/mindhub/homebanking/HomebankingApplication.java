package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;


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
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository repositoryAccount) {
		return (args) -> {
			Account firstAccount = new Account("VIN001", this.localDateTime, 5000);
			Account secondAccount = new Account("VIN002", this.localDateTimeNextDay, 7500);
			Account thirdAccount = new Account("VIN003", this.localDateTimeTwoDays, 8500);
			Account fourthAccount = new Account("VIN004", this.localDateTimeThreeDays, 900);
			Client melbaMorel = new Client("Melba", "Morel", "melba@mindhub.com");
			Client darylBaptie = new Client("Daryl", "Baptie", "darylBaptie@gmail.com");
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
			;
		};
	}








}
