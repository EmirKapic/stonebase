A Spring Data JPA Wrapper that allows for easier filtering, sorting, pagination etc.
Generally can replace Spring Data methods in almost all cases.

The library currently relies on `Specification` API and as such it requires a Spring repository. For this reason the
`BaseRepository` interface was made which all your individual repositories should implement. For most simple use cases
you will not need to modify the inherited methods in any way and should instead rely on `FilterBuilders`.

Usage example:

```java
List<Model> models = modelRepository.findAll(
    modelRepository.filterBuilder()
            .with(ModelField.ID, ConditionType.GREATER_THAN, 5)
            .with(ModelField.SOME_FIELD, ConditionType.LESS_THAN, 3)
);
```
This example snippet searches for all Models in your database with ID field greater than 5 and SOME_FIELD field less than 3.
The `ModelField` enum classes are automatically generated based on your classes. For this reason all of your hibernate model classes
aka classes annotated with `@Entity` should also be annotated with `@Model`.

```java
@Entity
@Model
class Model {
	//...
}
```

Library offers a wide array of `ConditionTypes`. It also offers `and` and `or` methods for more complex querying e.g.
```java
List<Model> models = modelRepository.findAll(
    modelRepository.filterBuilder()
            .with(ModelField.OTHER_FIELD, ConditionType.EQUAL, 10)
            .or()
                .with(ModelField.ID, ConditionType.GREATER_THAN, 5)
                .with(ModelField.SOME_FIELD, ConditionType.LESS_THAN, 3)
            .end()
);
```
This query finds all models with OTHER_FIELD = 10 and (ID > 5 or SOME_FIELD < 3). Everything between the `or` and
`end` method is considered part of the disjunction.

##### TODO: More README examples.

### Development TODO
- Move away from relying on Spring Data JPA's `Specification` API. This would however require implementing Projections,
Sorting, Pagination etc. on our own so in this first version it is not done.
- Add support for stored procedures
- Add support for json fields