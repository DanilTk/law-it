### <b>Title</b> : Using YAML configuration files

<b>Date</b> : 06.04.2021   
<b>Author</b> : Jarosław Gołuchowski  
<b>Description</b> : With switching to new architecture model we have to refactor usage of application properties. I
suggest using YAML configuration files which can contain multiple profiles used on different environments (local, test,
prod). It will allow us to keep properties for different environments in separate files and also to easly map properties
to Java objects. [Link](https://www.baeldung.com/spring-yaml).

Currently, we have multiple files, we can replace it with mutliple profiles in one file or keep each profile in seperate
file:

```yaml
spring:
    config:
        activate:
            on-profile: local
name: test-YAML
environment: testing
enabled: false
servers: 
    - www.abc.test.com
    - www.xyz.test.com

---
spring:
    config:
        activate:
            on-profile: test
name: prod-YAML
environment: production
enabled: true
servers: 
    - www.abc.com
    - www.xyz.com

```