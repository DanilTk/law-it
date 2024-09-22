<h3><b>Title</b> : Optional domain fields</h3>
<b>Date</b> : 14.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

Each optional field in domain object should be vavr's Option, and contain @NonNull and @Builder.Default annotation.

Example:

```java
@Data
@SuperBuilder
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel {
	
	@NonNull
	@Builder.Default
	private final Option<PasswordResetToken> passwordResetToken = Option.none();
	
	@NonNull
	@Builder.Default
	private final Option<ActivationToken> activationToken = Option.none();
	
}
```

This solution gives us ability to set those fields to Option.none() when the value is not provided in the build() method
sequence. At the same time we are blocking user from giving us null value directly. So we will have only Option.some()
and Option.none()
values, instead of null.

