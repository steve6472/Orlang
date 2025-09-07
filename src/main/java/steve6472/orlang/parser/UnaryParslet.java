package steve6472.orlang.parser;

import steve6472.core.tokenizer.PrefixParselet;
import steve6472.core.tokenizer.Token;
import steve6472.core.tokenizer.TokenParser;
import steve6472.core.tokenizer.Tokenizer;
import steve6472.orlang.AST;
import steve6472.orlang.OrlangPrecedence;
import steve6472.orlang.OrlangToken;
import steve6472.orlang.ParserException;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public class UnaryParslet implements PrefixParselet<AST.Node>
{
    @Override
    public AST.Node parse(Tokenizer tokenizer, TokenParser<AST.Node> parser)
    {
        Token type = tokenizer.getCurrentToken().type();
        if (!(type instanceof OrlangToken token))
            throw new ParserException("Token is not of orlang type");
        if (!token.forUnary)
            throw new ParserException("Token '" + token + "' is not for unary operation");
        return new AST.Node.UnaryOp(token, parser.parse(OrlangPrecedence.PREFIX));
    }
}
