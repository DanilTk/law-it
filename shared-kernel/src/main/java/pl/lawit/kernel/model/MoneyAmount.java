package pl.lawit.kernel.model;

import lombok.NonNull;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;
import static pl.lawit.kernel.validation.ValidationUtils.require;

public record MoneyAmount(

	@NonNull
	BigDecimal value

) implements Comparable<MoneyAmount> {

	public MoneyAmount {
		require(value.compareTo(BigDecimal.ZERO) >= 0, "value cannot be below 0");
		require(value.scale() <= 2, "scale cannot exceed 2");
	}

	public static MoneyAmount of(@NonNull BigDecimal value) {
		BigDecimal roundedValue = value.setScale(2, HALF_UP);
		return new MoneyAmount(roundedValue);
	}

	public MoneyAmount add(MoneyAmount other) {
		return MoneyAmount.of(value.add(other.value));
	}

	public MoneyAmount subtract(MoneyAmount other) {
		return MoneyAmount.of(value.subtract(other.value));
	}

	public MoneyAmount multiply(BigDecimal multiplier) {
		return MoneyAmount.of(value.multiply(multiplier).setScale(2, HALF_EVEN));
	}

	@Override
	public int compareTo(MoneyAmount other) {
		return value.compareTo(other.value);
	}

	private String formatValue(BigDecimal bd) {
		return bd.stripTrailingZeros().toPlainString();
	}
	@Override
	public String toString() {
		return  formatValue(value);
	}

}
