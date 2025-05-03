package com.example.digitalbanking.services;

import com.example.digitalbanking.dtos.CustomerDTO;
import com.example.digitalbanking.entity.*;
import com.example.digitalbanking.enums.OperationType;
import com.example.digitalbanking.exceptions.BalanceNotSufficientException;
import com.example.digitalbanking.exceptions.BankAccountNotFoundException;
import com.example.digitalbanking.exceptions.CustomerNotFoundException;
import com.example.digitalbanking.mappers.BankAccountMapperImp;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankAccountRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImp implements BankAccountService{
    private AccountOperationRepository accountOperationRepository;
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private BankAccountMapperImp dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        log.info("saving new customer...");
        Customer toBeSavedCustomer = dtoMapper.fromCustomerDTO(customer);
        Customer savedCustomer = customerRepository.save(toBeSavedCustomer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("customer not found");
        }
        CurrentAccount bankAccount = new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCustomer(customer);
        bankAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return savedBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("customer not found");
        }
        SavingAccount bankAccount = new SavingAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCustomer(customer);
        bankAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return savedBankAccount;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customersDTO = customers
                .stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customersDTO;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("bank account not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccount(accountId);
        if(bankAccount.getBalance()<amount) {
            throw new BalanceNotSufficientException("balance not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to "+accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList() {
        return bankAccountRepository.findAll();
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("customer not found"));
        return dtoMapper.fromCustomer(customer);
    }
}
