<h3><b>Title</b> : Access modifiers and package organisation </h3>
<b>Date</b> : 07.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

1. Components related to same business logic should be stored in the same package
2. Only entry-point component should have public scope
3. Other components should have package-private scope

Example:

```

        ├── product
        │   ├── ProductDetailsAppliactionService.java (public)
        │   ├── ProductDetailsEditionService.java (package-private)
        │   ├── ProductDetailsSubmissionService.java (package-private)
        │   └── ProductDetailsValidator.java (package-private)
        │   └── ProductDetailsInfoResolver.java (package-private)
        └── user
            ├── UserDetailsAppliactionService.java (public)
            ├── UserDetailsEditionService.java (package-private)
            ├── UserDetailsSubmissionService.java (package-private)
            └── UserDetailsValidator.java (package-private)
            └── UserDetailsInfoResolver.java (package-private)
```



