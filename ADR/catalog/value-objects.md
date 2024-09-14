<h3><b>Title</b> : Value objects</h3>
<b>Date</b> : 06.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

Instead of using primitive data types or their object-oriented equivalents such as String, int, Integer, Long we should
create a specific Value Object that contains the value and creation logic constraints. Creation logic constraints and
validation have to be simple and can't involve additional component. Any complex and sophisticated validation should be
done in external components.

```java

public class Pesel {
	
	private final String value;
	
	private Pesel(String value) {
			validateFormat(value);
			this.value = value;
	}
	
	public static Pesel of(String value){
		return new Pesel(value);
    }
    
    private void validateFormat(String value){
		//
        //if not valid throws an exception
        throw new IllegalArgumentException("Not valid pesel");
    }
}
```

or Value Objects can contain other Value Objects

```java

@Data(staticConstructor = "of")
@Accessors(fluent = true)
public class Country {
	
	@NonNull
	private final CountryCode code;
	
	@NonNull
	private final CountryName name;
}
```


