package steve6472.orlang;

import steve6472.core.tokenizer.Token;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public enum OrlangToken implements Token
{
    DOT("."),
    ASSIGN("="),
    SEMICOLON(";"),
    COMMA(","),
    QUESTION("?"),
    COLON(":"),

    // Logical operators
    NOT("!", null, true),
    OR("||", OrlangPrecedence.OR),
    AND("&&", OrlangPrecedence.AND),

    // Equality
    LESS("<", OrlangPrecedence.COMPARE),
    LESS_EQ("<=", OrlangPrecedence.COMPARE),
    GREATER(">", OrlangPrecedence.COMPARE),
    GREATER_EQ(">=", OrlangPrecedence.COMPARE),
    EQUAL("==", OrlangPrecedence.COMPARE),
    NOT_EQUAL("!=", OrlangPrecedence.COMPARE),

    // Math
    MOD("%", OrlangPrecedence.PRODUCT),
    MUL("*", OrlangPrecedence.PRODUCT),
    DIV("/", OrlangPrecedence.PRODUCT),
    ADD("+", OrlangPrecedence.SUM),
    SUB("-", OrlangPrecedence.SUM, true),

    PARENTHESIS_LEFT("("),
    PARENTHESIS_RIGHT(")"),

    RETURN("return"),
    TRUE("true"),
    FALSE("false")
    ;

    private final String symbol;
    public final OrlangPrecedence binaryPrecedence;
    public final boolean forUnary;

    OrlangToken(String symbol, OrlangPrecedence binaryPrecedence, boolean forUnary)
    {
        this.symbol = symbol;
        this.binaryPrecedence = binaryPrecedence;
        this.forUnary = forUnary;
    }

    OrlangToken(String symbol)
    {
        this(symbol, null, false);
    }

    OrlangToken(String symbol, OrlangPrecedence binaryPrecedence)
    {
        this(symbol, binaryPrecedence, false);
    }

    @Override
    public String getSymbol()
    {
        return symbol;
    }
}
