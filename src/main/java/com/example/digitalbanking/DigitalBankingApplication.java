package com.example.digitalbanking;

import com.example.digitalbanking.dtos.CustomerDTO;
import com.example.digitalbanking.entity.*;
import com.example.digitalbanking.enums.AccountStatus;
import com.example.digitalbanking.enums.OperationType;
import com.example.digitalbanking.exceptions.BalanceNotSufficientException;
import com.example.digitalbanking.exceptions.BankAccountNotFoundException;
import com.example.digitalbanking.exceptions.CustomerNotFoundException;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankAccountRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import com.example.digitalbanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}

//	@Bean
	public CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository) {
		return args -> {
			BankAccount bankAccount = bankAccountRepository.findById("319b80a9-552e-4e88-b999-a483368114ce").orElse(null);
			if(bankAccount != null) {
				System.out.println("*****************************");
				System.out.println(bankAccount.getId());
				System.out.println(bankAccount.getBalance());
				System.out.println(bankAccount.getStatus());
				System.out.println(bankAccount.getCreateAt());
				System.out.println(bankAccount.getCustomer().getName());
				System.out.println(bankAccount.getClass().getSimpleName());
				if(bankAccount instanceof CurrentAccount) {
					System.out.println("over draft => "+((CurrentAccount)bankAccount).getOverDraft());
				} else if(bankAccount instanceof SavingAccount) {
					System.out.println("rate => "+((SavingAccount)bankAccount).getInterestRate());
				}
				bankAccount.getAccountOperations().forEach(operation -> {
					System.out.println("==========================");
					System.out.println(operation.getType() + "\t" + operation.getDate() + "\t" + operation.getAmount());
				});
			}
		};
	}
//	@Bean
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

				bankAccountRepository.findAll().forEach(acc->{
					for (int i = 0; i <10 ; i++) {
						AccountOperation accountOperation=new AccountOperation();
						accountOperation.setDate(new Date());
						accountOperation.setAmount(Math.random()*12000);
						accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
						accountOperation.setBankAccount(acc);
						accountOperationRepository.save(accountOperation);
					}
				});

			});
		};
	}

	@Bean
	CommandLineRunner runner(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("hassan", "iman", "mohammed").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5, customer.getId());
					List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
					for(BankAccount bankAccount: bankAccounts) {
						for(int i=0; i<10 ; i++) {
							bankAccountService.credit(bankAccount.getId(), 10000 + Math.random() * 120000, "Credit");
							bankAccountService.debit(bankAccount.getId(), 1000 + Math.random() * 9000, "Debit");
						}
					}
                } catch (CustomerNotFoundException | BankAccountNotFoundException | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            });
		};
	}
}
