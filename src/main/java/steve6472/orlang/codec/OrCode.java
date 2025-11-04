package steve6472.orlang.codec;

import com.mojang.serialization.Codec;
import steve6472.orlang.AST;
import steve6472.orlang.Orlang;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public record OrCode(List<AST.Node> code, String codeStr)
{
    public static final Codec<OrCode> CODEC = Codec.withAlternative(
        Codec.STRING.xmap(Orlang.parser::parse, OrCode::codeStr),
        Codec.STRING.listOf().xmap(list -> Orlang.parser.parse(list.stream().map(s -> s.endsWith(";") ? s : s + ";").collect(Collectors.joining())), c -> List.of(c.codeStr.split(";")))
    );
}
