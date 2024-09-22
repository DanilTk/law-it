<h3><b>Title</b> : Vavr collections</h3>
<b>Date</b> : 06.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

We should avoid using Java's native Collections. Instead, we should use vavr.

* Java’s standard collection are mutable what can lead to a lot of troubles (not only with thread safety). Java provides
  immutable collections like unmodifiableList, but they still have methods like add, remove etc. which can be used by the
  programmer, and it can explode at runtime.
* Vavr implemented collection from scratch. They do not implement Java collection interfaces from java.util, so they have
  different
  (better) API. They are based on java.lang.Iterable.
* Vavr’s collection are immutable - cannot be modified after their creation. They are also persistent which means that
  they preserve the previous version of itself when being modified. All modifications of Vavr’s collections are not
  prohibited but simply return new version of themselves. It does not mean that they copy all stored values. All new
  versions share an immutable memory between instances. It makes them thread safe.
* Java’s stream increased a number of operation for collections, but it is boilerplate (you always need to call stream()
  method at the beginning and collect()
  at the end). Vavr’s collection API has much more operations than Java’s stream (without boilerplate). I encourage you
  to take a look at documentation

[Vavr Documantation](https://docs.vavr.io/)

[Vavr Tutorial](https://www.baeldung.com/vavr-collections)

[More Vavr Examples](https://blog.pragmatists.com/functional-programming-in-java-with-vavr-9861e704301c)



