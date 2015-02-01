package org.decimal4j.arithmetic;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.decimal4j.scale.ScaleMetrics;
import org.decimal4j.truncate.DecimalRounding;
import org.decimal4j.truncate.OverflowMode;

/**
 * An arithmetic implementation which truncates decimals after the last scale
 * digit without rounding. Operations are unchecked, that is, the result of an
 * operation that leads to an overflow is silently truncated.
 */
public class UncheckedScaleNfTruncatingArithmetics extends
		AbstractUncheckedScaleNfArithmetics implements DecimalArithmetics {

	/**
	 * Constructor for silent decimal arithmetics with given scale, truncating
	 * {@link RoundingMode#DOWN DOWN} rounding mode and
	 * {@link OverflowMode#UNCHECKED SILENT} overflow mode.
	 * 
	 * @param scaleMetrics
	 *            the scale, a non-negative integer denoting the number of
	 *            digits to the right of the decimal point
	 * @throws IllegalArgumentException
	 *             if scale is negative or uneven
	 */
	public UncheckedScaleNfTruncatingArithmetics(ScaleMetrics scaleMetrics) {
		super(scaleMetrics);
	}

	@Override
	public RoundingMode getRoundingMode() {
		return RoundingMode.DOWN;
	}

	@Override
	public long multiply(long uDecimal1, long uDecimal2) {
		return Mul.multiply(this, uDecimal1, uDecimal2);
	}

	@Override
	public long square(long uDecimal) {
		return Mul.square(getScaleMetrics(), uDecimal);
	}

	@Override
	public long sqrt(long uDecimal) {
		return Sqrt.sqrt(this, uDecimal);
	}

	@Override
	public long divideByLong(long uDecimalDividend, long lDivisor) {
		return uDecimalDividend / lDivisor;
	}

	@Override
	public long divide(long uDecimalDividend, long uDecimalDivisor) {
		return Div.divide(this, uDecimalDividend, uDecimalDivisor);
	}
	
	@Override
	public long invert(long uDecimal) {
		return Invert.invert(this, uDecimal);
	}

	@Override
	public long avg(long a, long b) {
		return Avg.avg(a, b);
	}

	@Override
	public long multiplyByPowerOf10(long uDecimal, int positions) {
		return Pow10.multiplyByPowerOf10(uDecimal, positions);
	}

	@Override
	public long divideByPowerOf10(long uDecimal, int positions) {
		return Pow10.divideByPowerOf10(uDecimal, positions);
	}

	@Override
	public long pow(long uDecimal, int exponent) {
		return Pow.pow(this, DecimalRounding.DOWN, uDecimal, exponent);
	}
	
	@Override
	public long shiftLeft(long uDecimal, int positions) {
		return Shift.shiftLeft(DecimalRounding.DOWN, uDecimal, positions);
	}

	@Override
	public long shiftRight(long uDecimal, int positions) {
		return Shift.shiftRight(DecimalRounding.DOWN, uDecimal, positions);
	}

	@Override
	public long round(long uDecimal, int precision) {
		return Round.round(this, uDecimal, precision);
	}

	@Override
	public long fromBigDecimal(BigDecimal value) {
		return value.multiply(getScaleMetrics().getScaleFactorAsBigDecimal()).longValue();
	}
	
	@Override
	public long fromDouble(double value) {
		return DoubleConversion.doubleToUnscaled(this, value);
	}

	@Override
	public long toLong(long uDecimal) {
		return getScaleMetrics().divideByScaleFactor(uDecimal);
	}

	@Override
	public float toFloat(long uDecimal) {
		return (float)toDouble(uDecimal);
	}

	@Override
	public double toDouble(long uDecimal) {
		return DoubleConversion.unscaledToDouble(this, uDecimal);
	}

	@Override
	public long parse(String value) {
		final int indexOfDot = value.indexOf('.');
		if (indexOfDot < 0) {
			return fromLong(Long.parseLong(value));
		}
		final long iValue;
		if (indexOfDot > 0) {
			//NOTE: here we handle the special case "-.xxx" e.g. "-.25"
			iValue = (indexOfDot == 1 && value.charAt(0) == '-') ? 0 : Long.parseLong(value.substring(0, indexOfDot));
		} else {
			iValue = 0;
		}
		final String fractionalPart = value.substring(indexOfDot + 1);
		final long fValue;
		final int fractionalLength = fractionalPart.length();
		if (fractionalLength > 0) {
			long fractionDigits = Long.parseLong(fractionalPart);
			final int scale = getScale();
			for (int i = fractionalLength; i < scale; i++) {
				fractionDigits *= 10;
			}
			for (int i = scale; i < fractionalLength; i++) {
				fractionDigits /= 10;
			}
			fValue = fractionDigits;
		} else {
			fValue = 0;
		}
		final boolean negative = iValue < 0 || value.startsWith("-");
		return iValue * one() + (negative ? -fValue : fValue);
	}

}