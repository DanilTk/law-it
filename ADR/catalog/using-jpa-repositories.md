### <b>Title</b> : Using JPA repositories

<b>Date</b> : 06.04.2021   
<b>Author</b> : Jarosław Gołuchowski  
<b>Description</b> : Instead of writing SQL queries inside code to retrieve some data we should
use [JPA repositories](https://spring.io/guides/gs/accessing-data-jpa/). It will make code cleaner and more readable. If
we'll need more complex queries we can use [Criteria Queries](https://www.baeldung.com/spring-data-criteria-queries).

Simple example without JPA repositories:

```java
@Repository
public class UsersListRepository {

    private final EntityManager em;

    public UsersListRepository(EntityManager em) {
        this.em = em;
    }

    public List<User> list() {
        return em.createQuery("SELECT u FROM User u WHERE u.deleted = FALSE ORDER BY u.firstName", User.class)
                .getResultList();
    }

}
```

Simple example without JPA repositories:

```java
@Repository
public interface UsersListRepository extends JpaRepository<User, Long> {
    List<User> findByDeleted(boolean deleted);
}
```