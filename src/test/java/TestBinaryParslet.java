import org.junit.Test;
import steve6472.orlang.Orlang;
import steve6472.orlang.OrlangToken;
import steve6472.orlang.codec.OrCode;

import java.util.List;

/**
 * Created by steve6472
 * Date: 8/31/2026
 * Project: Orlang <br>
 *
 */
public class TestBinaryParslet implements NodeExpectationHelper
{
    @Test
    public void parseBinary()
    {
        OrCode parsed = Orlang.parser.parse("1 + variable.t");

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.ADD, number(1), ident("variable.t"))
        );

        verify(spec, parsed.code());
    }

    @Test
    public void parseBinaryGroups()
    {
        OrCode parsed = Orlang.parser.parse("1 + (variable.t * 3)");

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.ADD, number(1), bin(OrlangToken.MUL, ident("variable.t"), number(3)))
        );

        verify(spec, parsed.code());
    }

    @Test
    public void constantFold()
    {
        OrCode parsed = Orlang.parser.parse("1 + 1");

        List<NodeExpectation> spec = List.of(
            number(2)
        );

        verify(spec, parsed.code());
    }

    @Test
    public void constantFoldGroups()
    {
        OrCode parsed = Orlang.parser.parse("1 + (2 * 3)");

        List<NodeExpectation> spec = List.of(
            number(7)
        );

        verify(spec, parsed.code());
    }

    @Test
    public void constantFoldWithMathCall()
    {
        OrCode parsed = Orlang.parser.parse("math.abs(-1) + (2 * 3)");

        List<NodeExpectation> spec = List.of(
            number(7)
        );

        verify(spec, parsed.code());
    }
}
