package steve6472.orlang.parser;

import steve6472.core.tokenizer.*;
import steve6472.orlang.AST;
import steve6472.orlang.OrlangToken;
import steve6472.orlang.ParserException;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public class BinaryParslet implements InfixParslet<AST.Node>
{
    private final Precedence precedence;
    private final TokenParser<AST.Node> parser;

    public BinaryParslet(Precedence precedence, TokenParser<AST.Node> parser)
    {
        this.precedence = precedence;
        this.parser = parser;
    }

    @Override
    public AST.Node parse(Tokenizer tokenizer, AST.Node left)
    {
        Token type = tokenizer.getCurrentToken().type();
        if (!(type instanceof OrlangToken token))
            throw new ParserException("Token is not of orlang type");
        if (token.binaryPrecedence == null)
            throw new ParserException("Token '" + token + "' is not for binary operation");
        AST.Node parsed = parser.parse(getPrecedence());
        if (parsed == null)
            throw new ParserException("null returned for right expression. Current precedence: " + precedence);

        // Constant fold
        AST.Node fold = constantFold(left, parsed, token);
        if (fold != null)
            return fold;

        return new AST.Node.BinOp(token, left, parsed);
    }

    private AST.Node constantFold(AST.Node left, AST.Node right, OrlangToken token)
    {
        if (left instanceof AST.Node.NumberLiteral(double leftVal) &&
            right instanceof AST.Node.NumberLiteral(double rightVal))
        {
            if (OrlangToken.FOLD_BINARY_MATH_RET_BOOL.contains(token))
            {
                boolean ret = switch (token)
                {
                    case LESS -> leftVal < rightVal;
                    case LESS_EQ -> leftVal <= rightVal;
                    case GREATER -> leftVal > rightVal;
                    case GREATER_EQ -> leftVal >= rightVal;
                    case EQUAL -> leftVal == rightVal;
                    case NOT_EQUAL -> leftVal != rightVal;
                    default -> throw new IllegalStateException("Unexpected value: " + token);
                };
                return new AST.Node.BoolLiteral(ret);
            }

            if (OrlangToken.FOLD_BINARY_MATH.contains(token))
            {
                double ret = switch (token)
                {
                    case MOD -> leftVal % rightVal;
                    case MUL -> leftVal * rightVal;
                    case DIV -> leftVal / rightVal;
                    case ADD -> leftVal + rightVal;
                    case SUB -> leftVal - rightVal;
                    default -> throw new IllegalStateException("Unexpected value: " + token);
                };
                return new AST.Node.NumberLiteral(ret);
            }
        }
        else if (OrlangToken.FOLD_BINARY_BOOL.contains(token) &&
            left instanceof AST.Node.BoolLiteral(boolean leftVal) &&
            right instanceof AST.Node.BoolLiteral(boolean rightVal))
        {
            boolean ret = switch (token)
            {
                case EQUAL -> leftVal == rightVal;
                case NOT_EQUAL -> leftVal != rightVal;
                case OR -> leftVal || rightVal;
                case AND -> leftVal && rightVal;
                default -> throw new IllegalStateException("Unexpected value: " + token);
            };
            return new AST.Node.BoolLiteral(ret);
        }

        return null;
    }

    @Override
    public Precedence getPrecedence()
    {
        return precedence;
    }
}
