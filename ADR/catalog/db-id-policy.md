<h3><b>Title</b> : Database id policy</h3>
<b>Date</b> : 06.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

Database ids should not be presented to end user. Technical/DB ids should be visible only in the infrastructure module.
Instead of presenting technical ids, each entity should have UUID variable responsible for interaction with end user.

Example

```java

public class User {
	
	private final Long id;
	
	private final UUID uuid;

}
```



