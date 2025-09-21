import org.junit.Test;
import steve6472.orlang.AST;
import steve6472.orlang.Orlang;
import steve6472.orlang.OrlangToken;
import steve6472.orlang.codec.OrCode;

import java.util.List;

/**
 * Created by steve6472
 * Date: 9/21/2025
 * Project: Orlang <br>
 */
public class TestMinus implements NodeExpectationHelper
{
    @Test
    public void test1()
    {
        String expression = "math.max(-math.sin(2), -0.95)";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            call("math.max",
                unary(OrlangToken.SUB,
                    call(
                        "math.sin",
                        number(2)
                    )
                ), number(-0.95)
            )
        );

        verify(spec, code);
    }

    @Test
    public void test2()
    {
        String expression = "-math.sin(2)";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            unary(OrlangToken.SUB,
                call(
                    "math.sin",
                    number(2)
                )
            )
        );

        verify(spec, code);
    }

    @Test
    public void test3()
    {
        String expression = "-math.sin(2) * 2";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.MUL,
                unary(OrlangToken.SUB,
                    call(
                        "math.sin",
                        number(2)
                    )
                ),
                number(2)
            )
        );

        verify(spec, code);
    }

    @Test
    public void test4()
    {
        String expression = "math.sin(2) * -2";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.MUL,
                call(
                    "math.sin",
                    number(2)
                ),
                number(-2)
            )
        );
        verify(spec, code);
    }

    @Test
    public void test5()
    {
        String expression = "-2";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            number(-2)
        );
        verify(spec, code);
    }

    @Test
    public void test6()
    {
        String expression = "-2 + 1";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.ADD,
                number(-2),
                number(1)
            )
        );
        verify(spec, code);
    }

    @Test
    public void test7()
    {
        String expression = "2 + -1";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.ADD,
                number(2),
                number(-1)
            )
        );
        verify(spec, code);
    }

    @Test
    public void test8()
    {
        String expression = "2 + - 1";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();
        System.out.println(code);

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.ADD,
                number(2),
                number(-1)
            )
        );
        verify(spec, code);
    }
}
