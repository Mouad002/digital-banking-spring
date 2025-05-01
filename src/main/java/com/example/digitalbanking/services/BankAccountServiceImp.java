package com.example.digitalbanking.services;

import com.example.digitalbanking.entity.BankAccount;
import com.example.digitalbanking.entity.Customer;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankAccountRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImp implements BankAccountService{
    private AccountOperationRepository accountOperationRepository;
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        return null;
    }

    @Override
    public BankAccount saveBankAccount(double initialBalance, String type, Long customerId) {
        return null;
    }

    @Override
    public List<Customer> getBankAccount(String accountId) {
        return List.of();
    }

    @Override
    public void debit(String accountId, double amount, String description) {

    }

    @Override
    public void credit(String accountId, double amount, String description) {

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) {

    }
}
