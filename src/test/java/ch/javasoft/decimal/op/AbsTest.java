package ch.javasoft.decimal.op;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ch.javasoft.decimal.Decimal;
import ch.javasoft.decimal.arithmetic.DecimalArithmetics;
import ch.javasoft.decimal.scale.ScaleMetrics;
import ch.javasoft.decimal.truncate.OverflowMode;

/**
 * Unit test for {@link Decimal#abs()}
 */
@RunWith(Parameterized.class)
public class AbsTest extends AbstractOneAryDecimalToDecimalTest {
	
	public AbsTest(ScaleMetrics scaleMetrics, OverflowMode overflowMode, DecimalArithmetics arithmetics) {
		super(arithmetics);
	}

	@Parameters(name = "{index}: {0}, {1}")
	public static Iterable<Object[]> data() {
		final List<Object[]> data = new ArrayList<Object[]>();
		for (final ScaleMetrics s : SCALES) {
			data.add(new Object[] {s, OverflowMode.UNCHECKED, s.getTruncatingArithmetics(OverflowMode.UNCHECKED)});
			data.add(new Object[] {s, OverflowMode.CHECKED, s.getTruncatingArithmetics(OverflowMode.CHECKED)});
		}
		return data;
	}

	@Override
	protected String operation() {
		return "abs";
	}
	
	@Override
	protected BigDecimal expectedResult(BigDecimal operand) {
		return operand.abs();
	}
	
	@Override
	protected <S extends ScaleMetrics> Decimal<S> actualResult(Decimal<S> operand) {
		if (isUnchecked() && rnd.nextBoolean()) {
			return operand.abs();
		}
		return operand.abs(getOverflowMode());
	}
}
