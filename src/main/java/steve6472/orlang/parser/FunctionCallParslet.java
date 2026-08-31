package steve6472.orlang.parser;

import steve6472.core.tokenizer.InfixParslet;
import steve6472.core.tokenizer.Precedence;
import steve6472.core.tokenizer.TokenParser;
import steve6472.core.tokenizer.Tokenizer;
import steve6472.orlang.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public class FunctionCallParslet implements InfixParslet<AST.Node>
{
    private final TokenParser<AST.Node> parser;

    public FunctionCallParslet(TokenParser<AST.Node> parser)
    {
        this.parser = parser;
    }

    @Override
    public AST.Node parse(Tokenizer tokenizer, AST.Node left)
    {
        if (!(left instanceof AST.Node.Identifier identifier))
            throw new ParserException("Left is not an Identifier");

        if (tokenizer.matchToken(OrlangToken.PARENTHESIS_RIGHT, true))
        {
            // Constant fold
            AST.Node fold = constantFold(identifier, new AST.Node[0]);
            if (fold != null)
                return fold;

            return new AST.Node.FunctionCall(identifier, new AST.Node[0]);
        }

        List<AST.Node> args = new ArrayList<>();

        while (true)
        {
            AST.Node arg = parser.parse(OrlangPrecedence.ANYTHING);
            args.add(arg);

            if (tokenizer.matchToken(OrlangToken.PARENTHESIS_RIGHT, true))
                break;

            if (!tokenizer.matchToken(OrlangToken.COMMA, true))
                throw new ParserException("Expected ',' or ')' in argument list, got " + tokenizer.peekToken());
        }

        AST.Node[] argsArray = args.toArray(new AST.Node[0]);

        // Constant fold
        AST.Node fold = constantFold(identifier, argsArray);
        if (fold != null)
            return fold;

        return new AST.Node.FunctionCall(identifier, argsArray);
    }

    private AST.Node constantFold(AST.Node.Identifier identifier, AST.Node[] args)
    {
        if (identifier.context() == VarContext.MATH)
        {
            OrlangValue[] argValues = new OrlangValue[args.length];

            for (int i = 0; i < args.length; i++)
            {
                AST.Node arg = args[i];
                if (!(arg instanceof AST.Node.NumberLiteral(double litValue)))
                    return null;
                argValues[i] = OrlangValue.num(litValue);
            }

            if (Orlang.FOLDABLE_MATH.contains(identifier.name()))
            {
                OrlangValue eval = Orlang.MATH_FUNCTIONS.get(identifier.name()).eval(argValues);
                if (eval instanceof OrlangValue.Number num)
                    return new AST.Node.NumberLiteral(num.value());
            }
        }
        return null;
    }

    @Override
    public Precedence getPrecedence()
    {
        return OrlangPrecedence.FUNC_CALL;
    }
}
