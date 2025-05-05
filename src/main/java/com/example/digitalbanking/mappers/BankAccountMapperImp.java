package com.example.digitalbanking.mappers;

import com.example.digitalbanking.dtos.AccountOperationDTO;
import com.example.digitalbanking.dtos.CurrentBankAccountDTO;
import com.example.digitalbanking.dtos.CustomerDTO;
import com.example.digitalbanking.dtos.SavingBankAccountDTO;
import com.example.digitalbanking.entity.AccountOperation;
import com.example.digitalbanking.entity.CurrentBankAccount;
import com.example.digitalbanking.entity.Customer;
import com.example.digitalbanking.entity.SavingBankAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImp {
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentBankAccount currentBankAccount) {
        CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentBankAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentBankAccount.getCustomer()));
        currentBankAccountDTO.setType(currentBankAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    public CurrentBankAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO) {
        CurrentBankAccount currentAccount=new CurrentBankAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingBankAccount savingBankAccount) {
        SavingBankAccountDTO savingBankAccountDTO=new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingBankAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingBankAccount.getCustomer()));
        savingBankAccountDTO.setType(savingBankAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    public SavingBankAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
        SavingBankAccount savingAccount=new SavingBankAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }
}
