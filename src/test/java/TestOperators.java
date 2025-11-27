import org.junit.Test;
import steve6472.orlang.*;
import steve6472.orlang.codec.OrCode;

import java.util.List;

/**
 * Created by steve6472
 * Date: 9/21/2025
 * Project: Orlang <br>
 */
public class TestOperators implements NodeExpectationHelper
{
    @Test
    public void testLogicalNot() {
        String expression = "!42";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            unary(OrlangToken.NOT, number(42))
        );

        verify(spec, code);
    }

    @Test
    public void testLogicalOr() {
        String expression = "1 || 0";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.OR, number(1), number(0))
        );

        verify(spec, code);
    }

    @Test
    public void testLogicalAnd() {
        String expression = "1 && 0";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.AND, number(1), number(0))
        );

        verify(spec, code);
    }




    @Test
    public void testLess() {
        String expression = "3 < 5";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.LESS, number(3), number(5))
        );

        verify(spec, code);
    }

    @Test
    public void testLessEq() {
        String expression = "3 <= 5";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.LESS_EQ, number(3), number(5))
        );

        verify(spec, code);
    }

    @Test
    public void testGreater() {
        String expression = "7 > 2";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.GREATER, number(7), number(2))
        );

        verify(spec, code);
    }

    @Test
    public void testGreaterEq() {
        String expression = "7 >= 2";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.GREATER_EQ, number(7), number(2))
        );

        verify(spec, code);
    }

    @Test
    public void testEqual() {
        String expression = "4 == 4";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.EQUAL, number(4), number(4))
        );

        verify(spec, code);
    }

    @Test
    public void testNotEqual() {
        String expression = "42 != 9";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.NOT_EQUAL, number(42), number(9))
        );

        verify(spec, code);
    }



    @Test
    public void testMod() {
        String expression = "10 % 3";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.MOD, number(10), number(3))
        );

        verify(spec, code);
    }

    @Test
    public void testMul() {
        String expression = "3 * 4";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.MUL, number(3), number(4))
        );

        verify(spec, code);
    }

    @Test
    public void testDiv() {
        String expression = "12 / 4";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.DIV, number(12), number(4))
        );

        verify(spec, code);
    }

    @Test
    public void testAdd() {
        String expression = "1 + 2";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.ADD, number(1), number(2))
        );

        verify(spec, code);
    }

    @Test
    public void testSub() {
        String expression = "5 - 3";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            bin(OrlangToken.SUB, number(5), number(3))
        );

        verify(spec, code);
    }

    @Test
    public void testUnarySub() {
        // because SUB has 'true' for unary capability
        String expression = "-8";
        OrCode parse = Orlang.parser.parse(expression);
        List<AST.Node> code = parse.code();

        List<NodeExpectation> spec = List.of(
            unary(OrlangToken.SUB, number(8))
        );

        verify(spec, code);
    }
}
