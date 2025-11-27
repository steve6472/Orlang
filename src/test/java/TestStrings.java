import org.junit.Test;
import steve6472.orlang.*;
import steve6472.orlang.codec.OrCode;

import java.util.List;

/**
 * Created by steve6472
 * Date: 9/21/2025
 * Project: Orlang <br>
 */
public class TestStrings implements NodeExpectationHelper
{
    @Test
    public void doubleQuotesParsing()
    {
        String expression = "query.argument(\"seed\")";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            call("query.argument",
                str("seed")
            )
        );

        verify(spec, code);
    }

    @Test
    public void singleQuotesParsing()
    {
        String expression = "query.argument('seed')";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            call("query.argument",
                str("seed")
            )
        );

        verify(spec, code);
    }

    @Test
    public void function()
    {
        String expression = "query.print(\"seed\")";
        OrCode parse = Orlang.parser.parse(expression);

        OrlangEnvironment environment = new OrlangEnvironment();
        environment.queryFunctionSet = new QueryFunctionSet()
        {{
            functions.put("print", OrlangValue.func(String.class, str -> {
                System.out.println(str);
                return 0;
            }));
        }};

        Orlang.interpreter.interpret(parse, environment);
    }

    @Test
    public void stringConcatenation()
    {
        String expression = "query.print('test: ' + 42)";
        OrCode parse = Orlang.parser.parse(expression);

        OrlangEnvironment environment = new OrlangEnvironment();
        environment.queryFunctionSet = new QueryFunctionSet()
        {{
            functions.put("print", OrlangValue.func(String.class, str -> {
                System.out.println(str);
                return 0;
            }));
        }};

        Orlang.interpreter.interpret(parse, environment);
    }
}
