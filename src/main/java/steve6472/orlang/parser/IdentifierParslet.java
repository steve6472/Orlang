package steve6472.orlang.parser;

import steve6472.core.tokenizer.MainTokens;
import steve6472.core.tokenizer.PrefixParselet;
import steve6472.core.tokenizer.TokenParser;
import steve6472.core.tokenizer.Tokenizer;
import steve6472.orlang.AST;
import steve6472.orlang.OrlangToken;
import steve6472.orlang.ParserException;
import steve6472.orlang.VarContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public class IdentifierParslet implements PrefixParselet<AST.Node>
{
    @Override
    public AST.Node parse(Tokenizer tokenizer, TokenParser<AST.Node> parser)
    {
        String contextSval = tokenizer.getCurrentToken().sval().toLowerCase(Locale.ROOT);
        VarContext context = VarContext.getContext(contextSval);
        if (context == null)
            throw new ParserException("Context '" + contextSval + "' is invalid");

        tokenizer.consumeToken(OrlangToken.DOT);
        String name = tokenizer.nextToken().sval();

        List<String> path = new ArrayList<>();
        while (tokenizer.matchToken(OrlangToken.DOT, true))
        {
            if (tokenizer.matchToken(MainTokens.NAME, true))
            {
                path.add(tokenizer.getCurrentToken().sval());
            }
        }

        return new AST.Node.Identifier(context, name, path.toArray(new String[0]));
    }
}
