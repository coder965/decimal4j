package ch.javasoft.decimal.arithmetic;

import ch.javasoft.decimal.OverflowMode;
import ch.javasoft.decimal.ScaleMetrics;

/**
 * Base class for arithmetic implementations implementing those functions where
 * rounding is no issue. Overflow is not checked, that is,
 * {@link #getOverflowMode()} returns {@link OverflowMode#STANDARD}.
 */
abstract public class AbstractUncheckedScaledArithmetics extends
		AbstractUncheckedArithmetics {

	private final ScaleMetrics scaleMetrics;
	private final int scale;
	private final long one;//10^scale

	public AbstractUncheckedScaledArithmetics(ScaleMetrics scaleMetrics) {
		this.scaleMetrics = scaleMetrics;
		this.scale = scaleMetrics.getScale();
		this.one = scaleMetrics.getScaleFactor();
	}

	@Override
	public ScaleMetrics getScaleMetrics() {
		return scaleMetrics;
	}

	@Override
	public int getScale() {
		return scale;
	}

	@Override
	public long one() {
		return one;
	}

	@Override
	public String toString(long uDecimal) {
		final int scale = getScale();
		final int negativeOffset = uDecimal < 0 ? 1 : 0;
		final StringBuilder sb = new StringBuilder(scale + 2 + negativeOffset);
		sb.append(uDecimal);
		final int len = sb.length();
		if (len <= scale + negativeOffset) {
			//Long.MAX_VALUE = 9,223,372,036,854,775,807
			sb.insert(negativeOffset, "0.00000000000000000000", 0, 2 + scale - len + negativeOffset);
		} else {
			sb.insert(len - scale, '.');
		}
		return sb.toString();
	}
}