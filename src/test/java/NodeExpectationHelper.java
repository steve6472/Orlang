import steve6472.orlang.AST;
import steve6472.orlang.OrlangToken;

import java.util.List;

/**
 * Created by steve6472
 * Date: 9/21/2025
 * Project: Orlang <br>
 */
public interface NodeExpectationHelper
{
    default NodeExpectation.IdentifierExp ident(String name) { return NodeExpectation.ident(name); }
    default NodeExpectation.NumberExp number(double v) { return NodeExpectation.number(v); }
    default NodeExpectation.BoolExp bool(boolean v) { return NodeExpectation.bool(v); }
    default NodeExpectation.StringExp str(String v) { return NodeExpectation.str(v); }
    default NodeExpectation.AssignExp assign(NodeExpectation id, NodeExpectation expr) { return NodeExpectation.assign(id, expr); }
    default NodeExpectation.FunctionCallExp call(String name, NodeExpectation... args) {
        return NodeExpectation.call(name, args);
    }
    default NodeExpectation.BinOpExp bin(OrlangToken token, NodeExpectation l, NodeExpectation r) { return NodeExpectation.bin(token, l, r); }
    default NodeExpectation.UnaryOpExp unary(OrlangToken token, NodeExpectation expr) { return NodeExpectation.unary(token, expr); }
    default NodeExpectation.TernaryExp ternary(NodeExpectation c, NodeExpectation t, NodeExpectation f) { return NodeExpectation.ternary(c, t, f); }
    default NodeExpectation.ReturnExp ret(NodeExpectation expr) { return NodeExpectation.ret(expr); }


    default void verify(List<NodeExpectation> expectations, List<AST.Node> nodes)
    {
        NodeExpectation.verify(expectations, nodes);
    }

    default void verify(List<NodeExpectation> expectations, List<AST.Node> nodes, Runnable doWhenFail)
    {
        try
        {
            NodeExpectation.verify(expectations, nodes);
        } catch (Exception ex)
        {
            doWhenFail.run();
            throw ex;
        }
    }
}
