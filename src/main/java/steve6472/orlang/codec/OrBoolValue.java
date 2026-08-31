package steve6472.orlang.codec;

import com.mojang.serialization.Codec;
import steve6472.orlang.OrlangEnvironment;

/**
 * Created by steve6472
 * Date: 8/31/2026
 * Project: Orlang <br>
 *
 */
public interface OrBoolValue
{
    Codec<OrBoolValue> CODEC = Codec.withAlternative(ConstantBool.CODEC, DynamicBool.CODEC);

    static OrBoolValue constant(boolean constant)
    {
        return constant ? ConstantBool.TRUE : ConstantBool.FALSE;
    }

    static OrBoolValue dynamic(OrCode code)
    {
        return new DynamicBool(code);
    }

    boolean hadFirstEval();
    boolean isConstant();
    boolean get();
    void evaluate(OrlangEnvironment environment);
    boolean evaluateAndGet(OrlangEnvironment environment);
    OrBoolValue copy();
}
