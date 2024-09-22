<h3><b>Title</b> : Immutable policy</h3>
<b>Date</b> : 06.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

POJO Objects should be immutable. We should avoid implementing methods that modifies the state of the object. Components
responsible for creating the object are Factories and Builders.

In case we want to modify existing object, we have to copy it or use
Lombok [@Wither](https://projectlombok.org/features/With) mechanism.

Anti-pattern example:

```java
private void rejectTooLowPrice(BigDecimal latestPrice) {
		ValidationErrors ve = new ValidationErrors();
		ve.rejectValue("price", "Price lower than latest", latestPrice);
		throw new ApiValidationException(ve);
		}

```



