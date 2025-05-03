package com.example.digitalbanking.services;

import com.example.digitalbanking.dtos.CustomerDTO;
import com.example.digitalbanking.entity.BankAccount;
import com.example.digitalbanking.entity.CurrentAccount;
import com.example.digitalbanking.entity.Customer;
import com.example.digitalbanking.entity.SavingAccount;
import com.example.digitalbanking.exceptions.BalanceNotSufficientException;
import com.example.digitalbanking.exceptions.BankAccountNotFoundException;
import com.example.digitalbanking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
}
