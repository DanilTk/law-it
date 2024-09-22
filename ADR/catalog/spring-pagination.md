<h3><b>Title</b> : Spring pagination </h3>
<b>Date</b> : 07.04.2021  <br>
<b>Author</b> : Tomasz Janicki  <br>
<b>Description</b> : <br>
We can use spring pageable interface instead of custom implementation. This will allow us to write sort on multiple
fields. <br>

```
http://localhost:8080/api/users?sort=name,asc&sort=id,desc
```

We can try override swagger docs with annotation <br>

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "Results page you want to retrieve"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "20", value = "Number of records per page."),
        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: fieldName,sortOrder. "
                + "Default sort order is ascending. Multiple sort criteria are supported.")})
public @interface ApiPageable {
}
```

Example usage <br>

```java
@ApiPageable
@GetMapping
public ResultPageDto<UserDto> list(UserFilterDto filter, @ApiIgnore Pageable pageable) {
	return listService.list(filter, pageable);
}
```
