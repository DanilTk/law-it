<h3><b>Title</b> : Time provider</h3>
<b>Date</b> : 12.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

In order to avoid static methods and be able to test features related to time, we should create and use component that
wraps static methods and returns current time.

``` java
@Component
@NoArgsConstructor
class TimeProvider {

    public Instant getInstant(){
        return Instant.now();
    }
}
```

Test usage:

``` groovy
def "should calculate first handover successfully"() {
    given:
    Instant instant = new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-31").toInstant()
    timeProvider.getInstant() >> instant
    ...
    ...
}
```






