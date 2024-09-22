### <b>Title</b> : Creating API objects based on domain provider

<b>Date</b> : 07.05.2021   
<b>Author</b> : Jarosław Gołuchowski  
<b>Description</b> : When creating RequestDto objects that are implementing Providers from domain module, you may
experience compilation error. Solution for it is adding ```java <? extends SomeProvider>``` to CommandProvider.

API object implementing domain provider:

```java
@Data
@Builder
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PACKAGE)
public class UserAccessTypeRequestDto implements UserAccessTypeProvider {
	
	@NotBlank
	private String userUuid;
	
	@NotNull
	private AccessType accessType;

}
```

Request that contains API object and implements CommandProvider interface :

```java
@Data
@Builder
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PACKAGE)
public class GrantFilePermissionsRequestDto implements GrantFilePermissionsCommandProvider {
	
	@NotBlank
	private String fileUuid;
	
	@NotNull
	private List<@Valid UserAccessTypeRequestDto> userPermissions;

}
```

CommandProvider interface

```java
public interface GrantFilePermissionsCommandProvider {

	String getFileUuid();
	
	List<? extends UserAccessTypeProvider> getUserPermissions();

}
```