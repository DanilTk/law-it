<h3><b>Title</b> : Spring dependency injection </h3>
<b>Date</b> : 07.04.2021  <br>
<b>Author</b> : Tomasz Janicki  <br>
<b>Description</b> : <br>
<b> Field injection is not recommended. </b> <br>
We should use only constructor injection is much easier to mock this in tests. <br>
The best option is to have only one constructor in component. <br>
Field injection in tests is acceptable. <br>

```java
@Service
public class FooService { 
    private final FooRepository repository;
	
    @Autowired
    public FooService(FooRepository repository) {
        this.repository = repository;
    }
}
```

From Spring 4.3 @Autowired annotation is not required because of implicit constructor injection
(only works with single constructor). <br>

Example usage with lombok

```java
@Service
@RequiredArgsConstructor
public class FooService {
    private final FooRepository repository;
}
```
