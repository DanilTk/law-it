### <b>Title</b> : Using SL4J annotation

<b>Date</b> : 07.04.2021   
<b>Author</b> : Jarosław Gołuchowski  
<b>Description</b> : Currently to log some message we create instanse of LOGGER object, with Loombok we can replace it
with annotation.

We have this:

```java
@Component
public class AppAuthEntryPoint implements AuthenticationEntryPoint {

	private static final Logger LOG = LoggerFactory.getLogger(AppAuthEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		
		LOG.info(
			"{} at {} {}",
			authException.getClass().getSimpleName(),
			request.getMethod(), request.getRequestURI()
		);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
```

We'll have this:

```java
@Component
@Slf4j
public class AppAuthEntryPoint implements AuthenticationEntryPoint {
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		
		log.info(
			"{} at {} {}",
			authException.getClass().getSimpleName(),
			request.getMethod(), request.getRequestURI()
		);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
```