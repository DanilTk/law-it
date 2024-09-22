<h3><b>Title</b> : Primitive types vs Objects</h3>
<b>Date</b> : 14.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

We should avoid using primitive types in domain/ infrastructure objects. Using Boolean instead of boolean gives as
ability<br>
to distinct if value was provided or not.

Example:

In that case we cannot force user to set/provide those fields using @NonNull annotation. Moreover, those fields will be
set to false by default (we don't want that).

```java
@Data
@SuperBuilder
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel {
	
	private final boolean activated;
	
	private final boolean deleted;
	
	private final boolean locked;
	
}
```

Solution:

```java
@Data
@SuperBuilder
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel {
	
	@NonNull
	private final Boolean activated;
	
	@NonNull
	private final Boolean deleted;
	
	@NonNull
	private final Boolean locked;
	
}
```

By changing type from boolen to Boolean, we are able to use @NonNull annotation, and force user to provide those fields,
even when he/she is using Builder. The Object will not be created unless logical variables are provided.


<h3> !!! Same situation with other primitive types such us int,long etc. !!! </h3>

