<h3><b>Title</b> : Using Lombok library</h3>
<b>Date</b> : 06.04.2021 <br>
<b>Author</b> : Tomasz Janicki  <br>
<b>Description</b> : <br>
Java library tool to generate boilerplate code like getters, setters and constructor by using some annotations. <br>
For more details see https://projectlombok.org/

Simple example without lombok: <br>

```java
public class User {
	
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	public User() {
	}
	
	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", age=" + age +
				'}';
	}
}
```

With lombok: <br>

```java
@Data
@NoArgsConstructor
public class User {
	
	private Long id;
	
	@NonNull
	private String firstName;
	
	@NonNull
	private String lastName;
}
```
