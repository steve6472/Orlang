import org.junit.Assert;
import org.junit.Test;
import steve6472.orlang.Orlang;
import steve6472.orlang.OrlangEnvironment;
import steve6472.orlang.OrlangValue;
import steve6472.orlang.codec.OrCode;

import java.util.List;

/**
 * Created by steve6472
 * Date: 8/31/2026
 * Project: Orlang <br>
 *
 */
public class TestFunctionCallParslet implements NodeExpectationHelper
{
    @Test
    public void constantFoldMathCallNoArgs()
    {
        OrCode parsed = Orlang.parser.parse("math.pi()");

        List<NodeExpectation> spec = List.of(
            number(Math.PI)
        );

        verify(spec, parsed.code());
    }

    @Test
    public void constantFoldMathCallArgs()
    {
        OrCode parsed = Orlang.parser.parse("math.abs(-5)");

        List<NodeExpectation> spec = List.of(
            number(5)
        );

        verify(spec, parsed.code());
    }

    @Test
    public void noConstantFoldMathRandom()
    {
        OrCode parsed = Orlang.parser.parse("math.random_integer(0, 100)");

        List<NodeExpectation> spec = List.of(
            call("math.random_integer", number(0), number(100))
        );

        verify(spec, parsed.code());

        OrlangValue testVal = null;

        for (int i = 0; i < 1000; i++)
        {
            OrlangValue interpreted = Orlang.interpreter.interpret(parsed, new OrlangEnvironment());
            if (testVal == null)
            {
                testVal = interpreted;
                continue;
            }

            if (!(testVal instanceof OrlangValue.Number first) || !(interpreted instanceof OrlangValue.Number current))
            {
                Assert.fail("Did not return numbers!");
                return;
            }

            if (first.value() != current.value())
                return;
        }

        Assert.fail("math.random_integer call did not return any other number than '" + testVal + "' in 1000 tries!");
    }
}
