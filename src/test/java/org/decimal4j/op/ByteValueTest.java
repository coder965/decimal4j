package org.decimal4j.op;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.decimal4j.Decimal;
import org.decimal4j.arithmetic.DecimalArithmetics;
import org.decimal4j.scale.ScaleMetrics;
import org.decimal4j.test.TestSettings;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit test for {@link Decimal#byteValue()} and
 * {@link Decimal#byteValueExact()}.
 */
@RunWith(Parameterized.class)
public class ByteValueTest extends Abstract1DecimalArgToAnyResultTest<Byte> {

	private final boolean exact;

	public ByteValueTest(ScaleMetrics scaleMetrics, boolean exact, DecimalArithmetics arithmetics) {
		super(arithmetics);
		this.exact = exact;
	}

	@Parameters(name = "{index}: scale={0}, exact={1}")
	public static Iterable<Object[]> data() {
		final List<Object[]> data = new ArrayList<Object[]>();
		for (final ScaleMetrics s : TestSettings.SCALES) {
			data.add(new Object[] { s, true, s.getDefaultArithmetics() });
			data.add(new Object[] { s, false, s.getDefaultArithmetics() });
		}
		return data;
	}

	@Override
	protected String operation() {
		return exact ? "byteValueExact" : "byteValue";
	}

	@Override
	protected Byte expectedResult(BigDecimal operand) {
		return exact ? operand.byteValueExact() : operand.byteValue();
	}

	@Override
	protected <S extends ScaleMetrics> Byte actualResult(Decimal<S> operand) {
		return exact ? operand.byteValueExact() : operand.byteValue();
	}
}