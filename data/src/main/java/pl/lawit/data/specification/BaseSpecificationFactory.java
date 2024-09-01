package pl.lawit.data.specification;

import io.vavr.collection.Traversable;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public abstract class BaseSpecificationFactory {

	private static final String WILDCARD = "%";

	protected <T> Specification<T> whereLikeIgnoreCase(
		SingularAttribute<T, String> attribute,
		@NonNull String value
	) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(
			criteriaBuilder.lower(root.get(attribute)),
			WILDCARD + value.toLowerCase() + WILDCARD
		);
	}

	protected <T, U> Specification<T> whereEquals(
		SingularAttribute<? super T, U> attribute,
		@NonNull U value
	) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
			root.get(attribute),
			value
		);
	}

	protected <T, U> Specification<T> whereNotEquals(
		SingularAttribute<? super T, U> attribute,
		@NonNull U value
	) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(
			root.get(attribute),
			value
		);
	}

	protected <T, U> Specification<T> whereIn(
		SingularAttribute<? super T, U> attribute,
		@NonNull Traversable<U> values
	) {
		if (values.isEmpty()) {
			// avoid condition of the form `WHERE value IN (NULL)` which might give unexpected results
			return constantBoolean(false);
		}
		return (root, query, criteriaBuilder) -> root.get(attribute).in(values.toJavaList());
	}

	protected <T, U> Specification<T> whereNotIn(
		SingularAttribute<? super T, U> attribute,
		@NonNull Traversable<U> values
	) {
		if (values.isEmpty()) {
			// avoid condition of the form `WHERE value IN (NULL)` which might give unexpected results
			return constantBoolean(false);
		}
		return (root, query, criteriaBuilder) -> criteriaBuilder.not(root.get(attribute).in(values.toJavaList()));
	}

	protected <T, U> Specification<T> whereChildIn(String attribute, String childAttribute,
												   @NonNull Traversable<U> values) {
		if (values.isEmpty()) {
			// avoid condition of the form `WHERE value IN (NULL)` which might give unexpected results
			return constantBoolean(false);
		}
		return (root, query, criteriaBuilder) -> root.get(attribute).get(childAttribute).in(values.toJavaList());
	}

	protected <T> Specification<T> constantBoolean(Boolean value) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(criteriaBuilder.literal(value));
	}

	protected <T> Specification<T> whereChildEquals(@NonNull String parentField, @NonNull String childField,
													@NonNull Object value) {
		return Specification.where((root, query, criteriaBuilder) -> criteriaBuilder
			.equal(root.join(parentField).get(childField), value));
	}

	protected <T> Specification<T> whereGrandChildEquals(@NonNull String parentField, @NonNull String childField,
														 @NonNull String grandChildField,
														 @NonNull Object value) {
		return Specification.where((root, query, criteriaBuilder) -> criteriaBuilder
			.equal(root.join(parentField).get(childField).get(grandChildField), value));
	}

	protected <T, U extends Comparable<? super U>> Specification<T> whereGreaterThanOrEqual(
		SingularAttribute<? super T, U> attribute,
		@NonNull U value
	) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
			root.get(attribute),
			value
		);
	}

	protected <T, U extends Comparable<? super U>> Specification<T> whereLessThanOrEqual(
		SingularAttribute<? super T, U> attribute,
		@NonNull U value
	) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
			root.get(attribute),
			value
		);
	}

	protected <T, U extends Comparable<? super U>> Specification<T> whereBetween(
		SingularAttribute<? super T, U> attribute,
		@NonNull U lowerBound,
		@NonNull U upperBound
	) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.between(
			root.get(attribute),
			lowerBound,
			upperBound
		);
	}

}
