import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import org.junit.Assert;
import org.junit.Test;
import steve6472.orlang.codec.OrBoolValue;
import steve6472.orlang.codec.OrNumValue;
import steve6472.orlang.codec.OrVec3;

import java.lang.reflect.Field;

/**
 * Created by steve6472
 * Date: 8/31/2026
 * Project: Orlang <br>
 *
 */
public class TestCodecs
{
    private static final String DYNAMIC_BOOL = "steve6472.orlang.codec.DynamicBool";
    private static final String DYNAMIC_NUM = "steve6472.orlang.codec.DynamicNum";
    private static final String CONSTANT_BOOL = "steve6472.orlang.codec.ConstantBool";
    private static final String CONSTANT_NUM = "steve6472.orlang.codec.ConstantNum";

    private static <T> T read(Codec<T> codec, String json)
    {
        JsonElement jsonElement = JsonParser.parseString(json);

        DataResult<Pair<T, JsonElement>> decode = codec.decode(JsonOps.INSTANCE, jsonElement);
        Pair<T, JsonElement> orThrow = decode.getOrThrow();

        return orThrow.getFirst();
    }

    /// Returns private variables from objects
    private static <T> T get(Object object, Class<T> type, String name)
    {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            Object o = field.get(object);
            return type.cast(o);
        } catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    private static void assertType(Object object, String className)
    {
        try
        {
            Class<?> clazz = Class.forName(className);
            if (!clazz.isAssignableFrom(object.getClass()))
            {
                Assert.fail("Expected type " + clazz.getSimpleName() + ", found " + object.getClass().getSimpleName());
            }
        } catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    /*
     * Bool
     */

    @Test
    public void testConstantBool()
    {
        OrBoolValue value = read(OrBoolValue.CODEC, "true");
        Assert.assertTrue(value.isConstant());
        assertType(value, CONSTANT_BOOL);
    }

    @Test
    public void testConstantBoolFold()
    {
        OrBoolValue value = read(OrBoolValue.CODEC, "'1 == 1'");
        Assert.assertTrue(value.isConstant());
        assertType(value, CONSTANT_BOOL);
    }

    @Test
    public void testDynamicBool()
    {
        OrBoolValue value = read(OrBoolValue.CODEC, "'1 == variable.t'");
        Assert.assertFalse(value.isConstant());
        assertType(value, DYNAMIC_BOOL);
    }

    /*
     * OrVec3
     */

    @Test
    public void testOrVec3()
    {
        OrVec3 value = read(OrVec3.CODEC, """
            {
                "x": 5,
                "y": "6",
                "z": "math.sin(temp.t)"
            }
            """);

        OrNumValue x = get(value, OrNumValue.class, "x");
        OrNumValue y = get(value, OrNumValue.class, "y");
        OrNumValue z = get(value, OrNumValue.class, "z");

        assertType(x, CONSTANT_NUM);
        assertType(y, CONSTANT_NUM);
        assertType(z, DYNAMIC_NUM);
    }
}
