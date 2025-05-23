# digital-banking-spring

## Overview
- this project is the backend of a project of digital banking, this backend is made with spring framework, while the front end will be made with angular framework.
- the goal of the whole project is to learn how to make apis for a front end like angular, and how to consume the api from the front end with angular at the same time.
- in this documentation I will focus on the things which are new like dtos and mappers. without diving deep into the concept which were covered in previous documentations.
## project structure
- the project structure is as the following
```
com.example.digitalbanking
|___dtos
    |___CustomerDTO
    ...
|___entities
    |___Customer
    ...
|___enums
    |___AccountStatus
    ...
|___exceptions
    |___BalanceNotSufficientException
    ...
|___mappers
    |___BankAccountMapperImp
|___repositories
    |___BankAccountRepository
    ...
|___services
    |___BankAccountService
    ...
|___web
    |___BankAccountRestApi
    ...    
```
## old things
in this structure we have samples of the class inside the packages and there are packages that we have seen in previous projects such :
- `entities` that contains the models of the whole project in this case they are `AccountOperation`, `Customer`, `CurrentBankAccount` and `SavingBankAccount` that inherit from `BankAccount`.
- `repositories` that contains the interface of spring data that handle the requests with the database. and in this project there are three, `AccountOperationRepository`, `BankAccountRepository` and `CustomerRepository`.
- `services` that contains the classes that will hold the business layer of the project, in this case there is `BankAccountService` interface and its implementation `BankAccountServiceImp`. 
- `web` finally this package contains the controllers, they hold methods that handle the communication with the user they are, `BankAccountRestApi` and `CustomerRestController`. 
## new things
### enums
- in this project we worked with `enums` like `AccountStatus` and `OperationType` they are perfect in reducing complexity, and not adding another classes just for types or status.
### Custom Exceptions
- the error that are related to business layer should be thrown as custom exception as a best practice, in our case we used many exception in different cases, like `BalanceNotSufficientException`.
### RestController
- in the controller we used `@RestController` annotation instead of `@Controller` which is important in case we are making apis that will be consumable with a front end like angular.
### Dtos
- let's say you have an entity with 100 attribute. it is so much of a mess if you used it in different controller or requests, because you might need only a few attributes in a request and different ones in another.
- `dto`s stands for data transfer object solve this problem perfectly, as they serve as java beans just like the models. each request has its own dto which can be transformed to the original model, and this transformation called `mapping`.
- the mapping is the hardest part since we should handle the transformation of each model for all the request that need in bidirectional way.
- this is a model called `Customer`.
```java
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BankAccount> accounts;
}
```
- this is an example of a `DTO`
```java
@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}
```
- and this is how the mapping is done
```java
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
```
end





