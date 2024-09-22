<h3><b>Title</b> : Builder and static constructor</h3>
<b>Date</b> : 06.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

<h3>Static constructor</h3>
For POJO objects with two or fewer fields we should use static constructor pattern with "of" prefix.

implementation:

```java

@Accessors(fluent = true)
@Value(staticConstructor = "of")
public class Person {
	
	@NonNull
	private final String name;
	
	@NonNull
	private final String surname;
	
}
```

usage:

```java
Person person= Person.of("Anna","Nowak");
```

<h3>Builder</h3>
For POJO objects with more than two fields we should use builder pattern.

implementation:

```java

@Accessors(fluent = true)
@Builder
@Data
public class Person {
	
	@NonNull
	private final String name;
	
	@NonNull
	private final String surname;
	
	@NonNull
	private final String secondName;
}
```

usage:

```java
Person person= Person.builder()
		.name("Jan")
		.surname("Kowalski")
		.secondName("Paweł")
		.build();

```




