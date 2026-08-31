package steve6472.orlang.codec;

import com.mojang.serialization.Codec;
import steve6472.orlang.OrlangEnvironment;

/**
 * Created by steve6472
 * Date: 8/31/2026
 * Project: Orlang <br>
 *
 */
public interface OrNumValue
{
    Codec<OrNumValue> CODEC = Codec.withAlternative(ConstantNum.CODEC, DynamicNum.CODEC);

    static OrNumValue constant(double constant)
    {
        return new ConstantNum(constant);
    }

    static OrNumValue dynamic(OrCode code)
    {
        return new DynamicNum(code);
    }

    boolean hadFirstEval();
    boolean isConstant();
    double get();
    float fget();
    void evaluate(OrlangEnvironment environment);
    double evaluateAndGet(OrlangEnvironment environment);
    OrNumValue copy();
}
