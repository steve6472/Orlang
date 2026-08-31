package steve6472.orlang;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public class Orlang
{
    public static final OrlangParser parser = new OrlangParser();
    public static final OrlangInterpreter interpreter = new OrlangInterpreter();

    public static final Map<String, OrlangValue.Func> MATH_FUNCTIONS = new HashMap<>();
    public static final Set<String> FOLDABLE_MATH = new HashSet<>();

    private static void regMath(String name, OrlangValue.Func function)
    {
        MATH_FUNCTIONS.put(name, function);
    }

    private static void regMathFold(String name, OrlangValue.Func function)
    {
        MATH_FUNCTIONS.put(name, function);
        FOLDABLE_MATH.add(name);
    }

    static
    {
        Class<Double> D = double.class;
        Class<Boolean> B = boolean.class;

        regMathFold("pi", OrlangValue.func(() -> Math.PI));

        // Basic arithmetic
        regMathFold("abs", OrlangValue.func(D, Math::abs));

        regMathFold("rad_sin", OrlangValue.func(D, Math::sin));
        regMathFold("rad_cos", OrlangValue.func(D, Math::cos));
        regMathFold("rad_asin", OrlangValue.func(D, Math::asin));
        regMathFold("rad_acos", OrlangValue.func(D, Math::acos));
        regMathFold("rad_atan", OrlangValue.func(D, Math::atan));
        regMathFold("rad_atan2", OrlangValue.func(D, D, Math::atan2));

        // Trigonometric functions (input in degrees, output where applicable also in degrees)
        regMathFold("sin", OrlangValue.func(D, v -> Math.sin(Math.toRadians(v))));
        regMathFold("cos", OrlangValue.func(D, v -> Math.cos(Math.toRadians(v))));
        regMathFold("asin", OrlangValue.func(D, v -> Math.toDegrees(Math.asin(v))));
        regMathFold("acos", OrlangValue.func(D, v -> Math.toDegrees(Math.acos(v))));
        regMathFold("atan", OrlangValue.func(D, v -> Math.toDegrees(Math.atan(v))));
        regMathFold("atan2", OrlangValue.func(D, D, (a, b) -> Math.toDegrees(Math.atan2(a, b))));

        regMathFold("ceil", OrlangValue.func(D, Math::ceil));
        regMathFold("floor", OrlangValue.func(D, Math::floor));
        regMathFold("trunc", OrlangValue.func(D, v -> (double) v.longValue()));
        regMathFold("round", OrlangValue.func(D, v -> (double) Math.round(v)));

        regMathFold("clamp", OrlangValue.func(D, D, D, Math::clamp));
        regMathFold("max", OrlangValue.func(D, D, Math::max));
        regMathFold("min", OrlangValue.func(D, D, Math::min));

        regMathFold("mod", OrlangValue.func(D, D, (a, b) -> a % b));
        regMathFold("pow", OrlangValue.func(D, D, Math::pow));
        regMathFold("sqrt", OrlangValue.func(D, Math::sqrt));
        regMathFold("exp", OrlangValue.func(D, Math::exp));
        regMathFold("ln", OrlangValue.func(D, Math::log));

        // Random functions
        regMath("random", OrlangValue.func(D, D, (low, high) -> low + Math.random() * (high - low)));
        regMath("random_integer", OrlangValue.func(D, D, (low, high) -> (double) ((int) (low + Math.random() * ((high - low) + 1)))));

        // Die rolls (sum of N randoms)
        regMath("die_roll", OrlangValue.func(D, D, D, (num, low, high) ->
        {
            double sum = 0;
            for (int i = 0; i < num.intValue(); i++)
            {
                sum += low + Math.random() * (high - low);
            }
            return sum;
        }));
        regMath("die_roll_integer", OrlangValue.func(D, D, D, (num, low, high) ->
        {
            int sum = 0;
            for (int i = 0; i < num.intValue(); i++)
            {
                sum += (int) (low + Math.random() * ((high - low) + 1));
            }
            return (double) sum;
        }));

        // Interpolation
        regMathFold("hermite_blend", OrlangValue.func(D, t -> t * t * (3 - 2 * t)));
        regMathFold("lerp", OrlangValue.func(D, D, D, (a, b, t) -> a + (b - a) * t));
        // Lerp for rotation (keeps shortest angle)
        // TODO: is this named wrong?
        regMathFold("deg_lerprotate", OrlangValue.func(D, D, D, (a, b, t) ->
        {
            double diff = ((b - a + Math.PI) % (2 * Math.PI)) - Math.PI;
            return a + diff * t;
        }));
        regMathFold("lerprotate", OrlangValue.func(D, D, D, (a, b, t) ->
        {
            double diff = ((b - a + 180.0) % 360.0) - 180.0;
            return a + diff * t;
        }));
    }

    public static boolean expectBool(OrlangValue value, OrlangToken operation)
    {
        if (!(value instanceof OrlangValue.Bool bool))
            throw new IllegalStateException("Operation '" + operation.getSymbol() + "' expected a boolean result, got: " + value);
        return bool.value();
    }

    public static boolean expectBool(OrlangValue value)
    {
        if (!(value instanceof OrlangValue.Bool bool))
            throw new IllegalStateException("Expected a boolean result, got: " + value);
        return bool.value();
    }

    public static double expectNum(OrlangValue value, OrlangToken operation)
    {
        if (!(value instanceof OrlangValue.Number num))
            throw new IllegalStateException("Operation '" + operation.getSymbol() + "' expected a number result, got: " + value);
        return num.value();
    }

    public static double expectNum(OrlangValue value)
    {
        if (!(value instanceof OrlangValue.Number num))
            throw new IllegalStateException("Expected a number result, got: " + value);
        return num.value();
    }
}
