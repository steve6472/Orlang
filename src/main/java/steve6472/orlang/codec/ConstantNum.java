package steve6472.orlang.codec;

import com.mojang.serialization.Codec;
import steve6472.orlang.OrlangEnvironment;

final class ConstantNum implements OrNumValue
{
    static final Codec<OrNumValue> CODEC = Codec.DOUBLE.xmap(ConstantNum::new, OrNumValue::get);

    private final double value;

    public ConstantNum(double value)
    {
        this.value = value;
    }

    @Override
    public boolean hadFirstEval()
    {
        return false;
    }

    @Override
    public boolean isConstant()
    {
        return true;
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
        // Empty because constants
    }

    @Override
    public double evaluateAndGet(OrlangEnvironment environment)
    {
        return value;
    }

    @Override
    public OrNumValue copy()
    {
        // We can just return this as constants are immutable
        return this;
    }

    @Override
    public String toString()
    {
        return "ConstantNum{" + value + '}';
    }
}