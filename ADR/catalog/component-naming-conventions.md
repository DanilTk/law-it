<h3><b>Title</b> : Components naming convention</h3>
<b>Date</b> : 06.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

<h4>Components naming conventions in modules:</h4>

* <b>api-*:</b>
    - RestController _(ProductSubmissionController)_:responsible for REST communication
    - RequestDto _(ProductSubmissionRequestDto)_ : input request data transfer object. <b> usually implements
      CommandProvider</b>
    - ResponseDto _(ProductSubmissionResponseDto)_ output response data transfer object
    - Handler _(ProductSubmissionHandler)_ : responsible for mapping RequestDto to domain objects and calling domain services
    - Factory _(ProductSubmissionResponseDtoFactory)_ : responsible for mapping RequestDto object to domain object ex.
      ProductSubmissionRequestDto -> ProductSubmissionCommand
* <b>domain:</b>
    - ApplicationService _(ProductSubmissionApplicationService)_ : entry point for business logic in domain module
    - Validator _(ProductSubmissionValidator)_ : responsible validation logic
    - Resolver _(ProductSubmissionResolver)_ : responsible for executing and resolving more complex logic
    - ValueObject _(ProductName)_: strong type approach. Instread of returning String, we should use value objects
    - Repository _(ProductRepository)_ : java interface which is implemented in the infrastructure module. Repository
      stands for any external communication
    - Command _(ProductSubmissionCommand)_ : command object
    - CommandProvider _(ProductSubmissionCommandProvider)_ : interface responsible for mapping API layer object to DOMAIN
      command objects
    - Factory _(ProductSubmissionCommandFactory)_ : responsible for object factorization. In more complex scenarios could use
      CommandProvider interface as an input
    - ValueObject/DomainObject - _(Product)_ : domain object representation
    - ScheduledTask - _(BirthdayNotificationScheduledTask)_ : responsible for schedule tasks logic
* <b>infrastructure:</b>
    - RestClient _(StripeRestClient)_ : responsible for REST communication with external services
    - RepositoryDb _(ProductInfoRepositoryDb)_: responsible for interaction with database, implements Repository
      interface from domain module
    - Repository{**integration-name-partner**} _(PaymentInfoRepositoryStripe)_ : implementing PaymentInfoRepository from
      domain and responsible for using invoking StripeRestClient methods
    - RequestDto _(PaymentRequestDto)_ : request data transfer object
    - ResponseDto _(PaymentInfoRequestDto)_ : response data transfer object
    - Factory _(PaymentRequestDtoFactory)_ : responsible for creating Request/Response Dto objects from domain objects
      and vice versa
    - Entity _(ProductEntity)_ : entity class

<u>Example:</u>

<h3>API Layer</h3>

``` java
public class EditProductRequestDto implements EditProductProvider{
  @NotNull
  @Schema(description = "product name", example = "Iphone")
  private final String productName;
}
```

<h3>DOMAIN Layer</h3>

``` java
public interface EditProductProvider{
  String getProductName();
}
```

``` java
@Builder
@Data
public class EditProductCommand{
  @NonNull
  private final String productName;
}
```

``` java
public class EditProductCommandFactory{
  public EditProductCommand create(EditProductProvider editProductProvider){
    return EditProductCommand.builder()
      .productName(editProductProvider.getProductName())
      ...
      ...
      .build();
  }
}
```
