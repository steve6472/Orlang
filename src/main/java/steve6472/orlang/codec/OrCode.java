package steve6472.orlang.codec;

import com.mojang.serialization.Codec;
import steve6472.orlang.AST;
import steve6472.orlang.Orlang;

import java.util.List;

/**
 * Created by steve6472
 * Date: 8/27/2025
 * Project: Orbiter <br>
 */
public record OrCode(List<AST.Node> code, String codeStr)
{
    public static final Codec<OrCode> CODEC = Codec.STRING.xmap(Orlang.parser::parse, OrCode::codeStr);
}
