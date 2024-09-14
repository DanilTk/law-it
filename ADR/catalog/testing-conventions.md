<h3><b>Title</b> : Testing conventions</h3>
<b>Date</b> : 06.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

1. [Spock](https://spockframework.org/spock/docs/1.3/all_in_one.html)  - is a default framework for writing tests.
2. Test naming conventions:

* <b>Unit test </b>- UserDetailsServiceTest.groovy - Test.groovy suffix. Responsible for testing a component logic in
  isolation, without launching a Sprint context
* <b>Integration test</b> - UserDetailsServiceIT.groovy - IT.groovy suffix. Responsible for testing a component in a
  module context. Relationships to external modules are mocked or stubbed. This test launches Spring context in tested
  module
* <b>Functional test</b> - EditUserDetailsControllerFT.groovy - FT.groovy suffix. Implemented only in application module
  and responsible for testing entire application flow.
* <b> API test </b> - EditUserDetailsControllerAPI.groovy - API.groovy suffix. Implemented only in api-* modules. Responsible for testing serialisation and deserialization only. Handlers are stubs/mocks.

3. Fakers<br>
   Fakers are classes responsible for creating static dummy object for testing purposes. Logic responsible for creating
   dummy test data should be implemented in Faker classes Example:

``` java
class ConstructionSiteFaker {

    static ConstructionSite CONSTRUCTION_SITE_WAWER = new ConstructionSite(CLIENT, "Wawer", "investorName", "addrStreetName",
            "addrStreetNumber", "addrCity", "addrPostCode", 12.0, 12.0)

    static ConstructionSite CONSTRUCTION_SITE_URSUS = new ConstructionSite(CLIENT, "Ursus", "investorName", "addrStreetName",
            "addrStreetNumber", "addrCity", "addrPostCode", 12.0, 12.0)

}
```

1. Mocking external API calls<br>
   External API calls should be mocked by Wiremock

2. Mocking and stubbing conventions per module

3. Component under test should be annotated with @Subject and named sut (subject under test)

``` java
class RefreshTokenEntityFactoryTest extends Specification {
	
	UserDeviceEntityFactory userDeviceEntityFactory = Stub()
	
	@Subject
	RefreshTokenEntityFactory sut = new RefreshTokenEntityFactory(userDeviceEntityFactory)
}
```

<h2>Unit tests conventions</h2>

* [api-modules]
    - domain layer = STUB/MOCK
* [domain]
    - infrastructure = STUB/MOCK
* [infrastructure]
    - RestClient = STUB/MOCK

<h2>Integration tests conventions</h2>
Launches spring context only in tested module
@SpringBean stub/mocks should be added in base abstract class to prevent starting multiple contexts

* [api-*]
    - domain layer = @SpringBean STUB/MOCK
* [domain]
    - infrastructure = @SpringBean STUB/MOCK
* [infrastructure]
    - RestClient = WIREMOCK
    - Database = H2/TestContainers

<h2>Functional tests conventions</h2>
Launches entire application

* [application]
    - RestClient = WIREMOCK
    - Database = H2/TestContainers

<h2>API tests conventions</h2>
Launches only api module context

* [api-*]
    - Handler = STUB/MOCK

1. Integration, API and Functional test should extend from test configuration class that aggregates all mocks and stubs

Example configuration in domain module:

``` groovy
class IntegrationTest extends Specification{

   @SpringBean
   ProductRepository productRepository;
}
```

``` groovy
class ProductFaker {

    static Product SAMPLE_PRODUCT = PRoduct.builder().id(1).name("Sample product").build();
}
```

``` groovy
class ProductServiceIT extends IntegrationTest {

    def "should return sample product"(){
    given:
      productRepository.findById(1) >> SAMPLE_PRODUCT
      ...
      ...
    }
}
```



