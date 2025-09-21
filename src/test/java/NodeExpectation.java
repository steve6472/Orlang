import steve6472.orlang.AST;
import steve6472.orlang.OrlangToken;

import java.util.List;
import java.util.Objects;

public sealed interface NodeExpectation permits
        NodeExpectation.IdentifierExp,
        NodeExpectation.NumberExp,
        NodeExpectation.BoolExp,
        NodeExpectation.StringExp,
        NodeExpectation.AssignExp,
        NodeExpectation.FunctionCallExp,
        NodeExpectation.BinOpExp,
        NodeExpectation.UnaryOpExp,
        NodeExpectation.TernaryExp,
        NodeExpectation.ReturnExp {

    void verify(AST.Node node);

    record IdentifierExp(String name) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.Identifier id))
            {
                throw new AssertionError("Expected Identifier, got " + node.getClass().getSimpleName());
            }
            if (!Objects.equals(name, id.stringify()))
            {
                throw new AssertionError("Expected Identifier name='" + name + "', got '" + id.name() + "'");
            }
        }
    }

    record NumberExp(double value) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.NumberLiteral num))
            {
                throw new AssertionError("Expected NumberLiteral, got " + node.getClass().getSimpleName());
            }
            if (Double.compare(value, num.value()) != 0)
            {
                throw new AssertionError("Expected NumberLiteral=" + value + ", got " + num.value());
            }
        }
    }

    record BoolExp(boolean value) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.BoolLiteral b))
            {
                throw new AssertionError("Expected BoolLiteral, got " + node.getClass().getSimpleName());
            }
            if (b.value() != value)
            {
                throw new AssertionError("Expected BoolLiteral=" + value + ", got " + b.value());
            }
        }
    }

    record StringExp(String value) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.StringLiteral s))
            {
                throw new AssertionError("Expected StringLiteral, got " + node.getClass().getSimpleName());
            }
            if (!Objects.equals(value, s.value()))
            {
                throw new AssertionError("Expected StringLiteral='" + value + "', got '" + s.value() + "'");
            }
        }
    }

    record AssignExp(NodeExpectation target, NodeExpectation expr) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.Assign a))
            {
                throw new AssertionError("Expected Assign, got " + node.getClass().getSimpleName());
            }
            target.verify(a.identifier());
            expr.verify(a.expression());
        }
    }

    record FunctionCallExp(String name, List<NodeExpectation> args) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.FunctionCall f))
            {
                throw new AssertionError("Expected FunctionCall, got " + node.getClass().getSimpleName());
            }
            if (!Objects.equals(name, f.identifier().stringify()))
            {
                throw new AssertionError("Expected FunctionCall to " + name + ", got " + f.identifier().name());
            }
            if (args.size() != f.arguments().length)
            {
                throw new AssertionError("Expected " + args.size() + " args, got " + f.arguments().length);
            }
            for (int i = 0; i < args.size(); i++)
            {
                args.get(i).verify(f.arguments()[i]);
            }
        }
    }

    record BinOpExp(OrlangToken token, NodeExpectation left, NodeExpectation right) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.BinOp b))
            {
                throw new AssertionError("Expected BinOp, got " + node.getClass().getSimpleName());
            }
            if (b.type() != token)
            {
                throw new AssertionError("Expected BinOp token=" + token + ", got " + b.type());
            }
            left.verify(b.left());
            right.verify(b.right());
        }
    }

    record UnaryOpExp(OrlangToken token, NodeExpectation expr) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.UnaryOp u))
            {
                throw new AssertionError("Expected UnaryOp, got " + node.getClass().getSimpleName());
            }
            if (u.type() != token)
            {
                throw new AssertionError("Expected UnaryOp token=" + token + ", got " + u.type());
            }
            expr.verify(u.expression());
        }
    }

    record TernaryExp(NodeExpectation cond, NodeExpectation ifTrue, NodeExpectation ifFalse) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.Ternary t))
            {
                throw new AssertionError("Expected Ternary, got " + node.getClass().getSimpleName());
            }
            cond.verify(t.condition());
            ifTrue.verify(t.ifTrue());
            ifFalse.verify(t.ifFalse());
        }
    }

    record ReturnExp(NodeExpectation expr) implements NodeExpectation
    {
        public void verify(AST.Node node)
        {
            if (!(node instanceof AST.Node.Return r))
            {
                throw new AssertionError("Expected Return, got " + node.getClass().getSimpleName());
            }
            expr.verify(r.expression());
        }
    }

    // --- Builder static helpers ---
    static IdentifierExp ident(String name) { return new IdentifierExp(name); }
    static NumberExp number(double v) { return new NumberExp(v); }
    static BoolExp bool(boolean v) { return new BoolExp(v); }
    static StringExp str(String v) { return new StringExp(v); }
    static AssignExp assign(NodeExpectation id, NodeExpectation expr) { return new AssignExp(id, expr); }
    static FunctionCallExp call(String name, NodeExpectation... args) {
        return new FunctionCallExp(name, List.of(args));
    }
    static BinOpExp bin(OrlangToken token, NodeExpectation l, NodeExpectation r) { return new BinOpExp(token, l, r); }
    static UnaryOpExp unary(OrlangToken token, NodeExpectation expr) { return new UnaryOpExp(token, expr); }
    static TernaryExp ternary(NodeExpectation c, NodeExpectation t, NodeExpectation f) { return new TernaryExp(c, t, f); }
    static ReturnExp ret(NodeExpectation expr) { return new ReturnExp(expr); }

    static void verify(List<NodeExpectation> expectations, List<AST.Node> nodes)
    {
        if (nodes.size() != expectations.size())
        {
            throw new AssertionError("Expected " + expectations.size() + " nodes, got " + nodes.size());
        }
        for (int i = 0; i < nodes.size(); i++)
        {
            expectations.get(i).verify(nodes.get(i));
        }
    }
}