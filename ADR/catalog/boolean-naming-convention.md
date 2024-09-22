<h3><b>Title</b> : Boolean fields naming convention</h3>
<b>Date</b> : 18.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

Boolean Java fields, methods returning boolean values and database boolean column names should have "is" prefix.<br>
Example:

```sql
<column name="is_activated" type="boolean" defaultValue="false">
<constraints nullable="false"/>
</column>

```

```java

@Data
@SuperBuilder(toBuilder = true)
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel {
	
	@NonNull
	private final Boolean isActivated;

}
```

```java
public class UserFactory {
	
	public User create() {
		return User.builder()
				.isActivated(provider.isActivated())
				.userRoles(provider.getUserRoles().map(userRoleFactory::create))
				.preferredLanguage(provider.getPreferredLanguage())
				.build();
	}

}
```



