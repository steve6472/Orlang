package steve6472.orlang;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 9/7/2025
 * Project: Orlang <br>
 */
public class QueryFunctionSet
{
    public final Map<String, OrlangValue.Func> functions = new HashMap<>();

    public static final QueryFunctionSet EMPTY = new QueryFunctionSet();
}
