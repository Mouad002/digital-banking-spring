package com.example.digitalbanking;

import com.example.digitalbanking.entity.CurrentAccount;
import com.example.digitalbanking.entity.Customer;
import com.example.digitalbanking.entity.SavingAccount;
import com.example.digitalbanking.enums.AccountStatus;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankAccountRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner start(CustomerRepository customerRepository,
								   BankAccountRepository bankAccountRepository,
								   AccountOperationRepository accountOperationRepository) {
		return args ->{
			Stream.of("hassan", "yassin", "aicha").forEach(name-> {
				Customer c1 = Customer.builder()
						.name(name)
						.email(name+"@gmail.com")
						.build();
				customerRepository.save(c1);

				customerRepository.findAll().forEach(customer -> {
					CurrentAccount ca = new CurrentAccount();
					ca.setId(UUID.randomUUID().toString());
					ca.setBalance(Math.random()*50000);
					ca.setCreateAt(new Date());
					ca.setStatus(AccountStatus.CREATED);
					ca.setCustomer(customer);
					ca.setOverDraft(7000);
					bankAccountRepository.save(ca);

					SavingAccount sa = new SavingAccount();
					sa.setId(UUID.randomUUID().toString());
					sa.setBalance(Math.random()*50000);
					sa.setCreateAt(new Date());
					sa.setStatus(AccountStatus.CREATED);
					sa.setCustomer(customer);
					sa.setInterestRate(5.5);
					bankAccountRepository.save(sa);
				});
			});
		};
	}
}
