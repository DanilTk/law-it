### <b>Title</b> : Feign as default rest client

<b>Date</b> : 07.04.2021   
<b>Author</b> : Jarosław Gołuchowski  
<b>Description</b> : With RestTemplate getting obsolete we need to choose which rest client we will use. I
propose [Feign](https://www.baeldung.com/spring-cloud-openfeign) as deafult rest client because it's really simple and
powerful tool.

Sample code :

```java
@FeignClient(value = "jplaceholder", url = "https://jsonplaceholder.typicode.com/")
public interface JSONPlaceHolderClient {

    @RequestMapping(method = RequestMethod.GET, value = "/posts")
    List<Post> getPosts();

    @RequestMapping(method = RequestMethod.GET, value = "/posts/{postId}", produces = "application/json")
    Post getPostById(@PathVariable("postId") Long postId);
}
```