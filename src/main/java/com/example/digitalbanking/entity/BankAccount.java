package com.example.digitalbanking.entity;

import com.example.digitalbanking.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class BankAccount {
    @Id
    private String id;
    private Date createAt;
    private double balance;
    private AccountStatus tatus;
    private String currency;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount")
    private List<Operation> operation;
}
