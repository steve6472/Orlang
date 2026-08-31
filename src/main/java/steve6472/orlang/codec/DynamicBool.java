package steve6472.orlang.codec;

import com.mojang.serialization.Codec;
import steve6472.orlang.AST;
import steve6472.orlang.Orlang;
import steve6472.orlang.OrlangEnvironment;
import steve6472.orlang.OrlangValue;

final class DynamicBool implements OrBoolValue
{
    static final Codec<OrBoolValue> CODEC = OrCode.CODEC
        .xmap(DynamicBool::new, o -> o.code)
        // This second xmap fixes issues where the value of number is stringified
        // Or after constant folding
        .xmap(dyn ->
        {
            if (dyn.code.code().getFirst() instanceof AST.Node.BoolLiteral(boolean val))
            {
                return OrBoolValue.constant(val);
            }
            return dyn;
        }, o -> (DynamicBool) o);

    private final OrCode code;
    private boolean value;
    private boolean hadFirstEval;

    public DynamicBool(OrCode code)
    {
        this.code = code;
    }

    public DynamicBool(OrCode code, boolean value, boolean hadFirstEval)
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
    public boolean get()
    {
        return value;
    }

    @Override
    public void evaluate(OrlangEnvironment environment)
    {
        OrlangValue retValue = Orlang.interpreter.interpret(code, environment);
        if (!(retValue instanceof OrlangValue.Bool bool))
            throw new RuntimeException("Orlang did not return a bool");
        this.value = bool.value();
        hadFirstEval = true;
    }

    @Override
    public boolean evaluateAndGet(OrlangEnvironment environment)
    {
        evaluate(environment);
        return get();
    }

    @Override
    public OrBoolValue copy()
    {
        return new DynamicBool(code, value, hadFirstEval);
    }

    @Override
    public String toString()
    {
        return "DynamicBool{" + "code=" + code + ", value=" + value + ", hadFirstEval=" + hadFirstEval + '}';
    }
}