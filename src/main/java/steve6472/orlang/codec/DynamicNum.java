package steve6472.orlang.codec;

import com.mojang.serialization.Codec;
import steve6472.orlang.AST;
import steve6472.orlang.Orlang;
import steve6472.orlang.OrlangEnvironment;
import steve6472.orlang.OrlangValue;

final class DynamicNum implements OrNumValue
{
    static final Codec<OrNumValue> CODEC = OrCode.CODEC
        .xmap(DynamicNum::new, o -> o.code)
        // This second xmap fixes issues where the value of number is stringified (happens as output from blockbench)
        // Or after constant folding
        .xmap(dyn ->
        {
            if (dyn.code.code().getFirst() instanceof AST.Node.NumberLiteral(double val))
            {
                return OrNumValue.constant(val);
            }
            return dyn;
        }, o -> (DynamicNum) o);

    private final OrCode code;
    private double value;
    private boolean hadFirstEval;

    public DynamicNum(OrCode code)
    {
        this.code = code;
    }

    public DynamicNum(OrCode code, double value, boolean hadFirstEval)
    {
        this.code = code;
        this.value = value;
        this.hadFirstEval = hadFirstEval;
    }

    @Override
    public boolean hadFirstEval()
    {
        return hadFirstEval;
    }

    @Override
    public boolean isConstant()
    {
        return false;
    }

    @Override
    public double get()
    {
        return value;
    }

    @Override
    public float fget()
    {
        return (float) value;
    }

    @Override
    public void evaluate(OrlangEnvironment environment)
    {
        OrlangValue retValue = Orlang.interpreter.interpret(code, environment);
        if (!(retValue instanceof OrlangValue.Number number))
            throw new RuntimeException("Orlang did not return a number, got '" + retValue + "' for code: '" + code.codeStr() + "'");
        this.value = number.value();
        hadFirstEval = true;
    }

    @Override
    public double evaluateAndGet(OrlangEnvironment environment)
    {
        evaluate(environment);
        return get();
    }

    @Override
    public OrNumValue copy()
    {
        return new DynamicNum(code, value, hadFirstEval);
    }

    @Override
    public String toString()
    {
        return "DynamicNum{" + "code=" + code + ", value=" + value + ", hadFirstEval=" + hadFirstEval + '}';
    }
}