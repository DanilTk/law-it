<h3><b>Title</b> : getBy() vs findBy()</h3>
<b>Date</b> : 18.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

While implementing functions that retrieve data, we should stick to naming rules:

- findBy() - returns Option or List
- getBy() - returns single Object

Example

```java

public interface UserRepository {
	
	User getByUUID(UUID uuid);
	
	Option<User> findByUsername(Username username);

	List<User> findAllByAge(int age);
	
}
```



