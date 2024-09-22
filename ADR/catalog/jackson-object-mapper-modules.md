<h3><b>Title</b> : Jackson Object mapper modules</h3>
<b>Date</b> : 07.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

We should register Object mapper modules for Jackson in order to marshall/unmarshall specific datatypes such us Option,
Optionals, etc.

Example:

```java

@Configuration
public class GeneralConfiguration {
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper()
				.registerModule(new ParameterNamesModule()
						.registerModule(new VavrModule())
						.registerModule(new Jdk8Module())
						.registerModule(new JavaTimeModule());
		return mapper;
	}
}
```

Usage:

```java
@Data
@Builder
@Schema(description = "Customer data")
public class AddUserInfoRequestDto {
	@NonNull
	private final String name;
	
	@NonNull
	@Builder.Default
	private final Option<String> motherName = Option.none();
}
```

[Example article](https://stackoverflow.com/questions/46285615/serializer-deserializer-for-vavr-objects)



