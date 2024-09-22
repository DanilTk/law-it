### <b>Title</b> : Separating controllers and API's

<b>Date</b> : 07.04.2021   
<b>Author</b> : Jarosław Gołuchowski  
<b>Description</b> : To make code more readable and clean we should split API definitions and controllers.

We have this:

```java
@RestController
@RequestMapping("animals")
@Api(tags = "_animals")
public class AnimalListController {

	private final AnimalListService listService;

	public AnimalListController(AnimalListService listService) {
		this.listService = listService;
	}

	@GetMapping
	@ApiOperation(value = "Get pageable list", notes = "Returns pageable list")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success")
	})
	public ResultPageDto<AnimalDto> list(AnimalFilterDto filter, PageRequestDto pageable) {
		log.info("Request animal list");
		return listService.list(filter, pageable.getPageable(10));
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Details", notes = "Returns animal details")
	@ApiResponses({
		@ApiResponse(code = RC.OK, message = "Success"),
		@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "Animal not found")
	})
	public AnimalDto details(@PathVariable(value = "id", required = true) Long id) {
		return listService.details(id);
	}

}
```

We'll have API interface:

```java
@Api(tags = "_animals")
public interface AnimalListControllerApi {
	
	@GetMapping
	@ApiOperation(value = "Get pageable list", notes = "Returns pageable list")
	@ApiResponses({
			@ApiResponse(code = RC.OK, message = "Success")
	})
	public ResultPageDto<AnimalDto> list(AnimalFilterDto filter, PageRequestDto pageable);
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Details", notes = "Returns animal details")
	@ApiResponses({
			@ApiResponse(code = RC.OK, message = "Success"),
			@ApiResponse(code = RC.RESOURCE_NOT_FOUND, message = "Animal not found")
	})
	public AnimalDto details(@PathVariable(value = "id", required = true) Long id);
	
}
```

and Controller class :

```java
@RestController
public class AnimalListController implements AnimalListControllerApi {

	private final AnimalListService listService;

	public AnimalListController(AnimalListService listService) {
		this.listService = listService;
	}

	public ResultPageDto<AnimalDto> list(AnimalFilterDto filter, PageRequestDto pageable) {
		return listService.list(filter, pageable.getPageable(10));
	}
	
	public AnimalDto details(@PathVariable(value = "id", required = true) Long id) {
		return listService.details(id);
	}

}
```