<h3><b>Title</b> : Mapping objects </h3>
<b>Date</b> : 07.04.2021  <br>
<b>Author</b> : Tomasz Janicki  <br>
<b>Description</b> : <br>
We can try to use the object mapping library from DTO to another object. <br>
Orika is one of those libraries. <br>

Article: https://www.baeldung.com/orika-mapping <br>
Docs: https://orika-mapper.github.io/orika-docs/ <br>

Simple test example <br>

```groovy
class BlogEntityMapperTest extends AbstractIntegrationSpecification {
    
	@Autowired
	MapperFacade mapperFacade
    
	def "should convert all fields"() {
        given:
        def givenBlogEntity = new BlogEntity(
                200L, 
                'title', 
                'link', 
                'lead', 
                20, 
                true, 
                LocalDateTime.of(2020, 2, 3, 8, 0, 0), 
                LocalDateTime.of(2020, 2, 3, 8, 0, 0), 
                LocalDateTime.of(2020, 3, 3, 8, 0, 0), 
                true
        )
        
        def expected = new BlogDomain(
                200L, 
                'title', 
                'link', 
                'lead', 
                20, 
                true, 
                LocalDateTime.of(2020, 2, 3, 8, 0, 0), 
                LocalDateTime.of(2020, 2, 3, 8, 0, 0), 
                LocalDateTime.of(2020, 3, 3, 8, 0, 0), 
                true
        )
        
        when:
        BlogDomain result = mapperFacade.map(givenBlogEntity, BlogDomain.class)
        
        then:
        result == expected 
    }
}
```

