package steve6472.orlang.parser;

import steve6472.core.tokenizer.InfixParslet;
import steve6472.core.tokenizer.Precedence;
import steve6472.core.tokenizer.TokenParser;
import steve6472.core.tokenizer.Tokenizer;
import steve6472.orlang.AST;
import steve6472.orlang.OrlangPrecedence;
import steve6472.orlang.ParserException;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public class AssignParslet implements InfixParslet<AST.Node>
{
    private final TokenParser<AST.Node> parser;

    public AssignParslet(TokenParser<AST.Node> parser)
    {
        this.parser = parser;
    }

    @Override
    public AST.Node parse(Tokenizer tokenizer, AST.Node left)
    {
        if (!(left instanceof AST.Node.Identifier identifier))
            throw new ParserException("Left is not an Identifier");

        return new AST.Node.Assign(identifier, parser.parse(getPrecedence()));
    }

    @Override
    public Precedence getPrecedence()
    {
        return OrlangPrecedence.ASSIGNMENT;
    }
}
