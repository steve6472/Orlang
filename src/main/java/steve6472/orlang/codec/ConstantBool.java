package steve6472.orlang.codec;

import com.mojang.serialization.Codec;
import steve6472.orlang.OrlangEnvironment;

final class ConstantBool implements OrBoolValue
{
    static final Codec<OrBoolValue> CODEC = Codec.BOOL.xmap(OrBoolValue::constant, OrBoolValue::get);

    static final ConstantBool TRUE = new ConstantBool(true);
    static final ConstantBool FALSE = new ConstantBool(false);

    private final boolean value;

    private ConstantBool(boolean value)
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
    public boolean get()
    {
        return value;
    }

    @Override
    public void evaluate(OrlangEnvironment environment)
    {
        // Empty because constants
    }

    @Override
    public boolean evaluateAndGet(OrlangEnvironment environment)
    {
        return value;
    }

    @Override
    public OrBoolValue copy()
    {
        // We can just return this as constants are immutable
        return this;
    }

    @Override
    public String toString()
    {
        return "ConstantBool{" + value + '}';
    }
}