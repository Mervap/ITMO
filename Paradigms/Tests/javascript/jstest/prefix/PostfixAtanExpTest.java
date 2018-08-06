package jstest.prefix;

import jstest.ArithmeticTests;
import jstest.BaseJavascriptTest;
import jstest.Language;
import jstest.object.ObjectExpressionTest;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PostfixAtanExpTest extends PrefixAtanExpTest {
    public static final BaseJavascriptTest.Dialect POSTFIX = BaseJavascriptTest.dialect(
            "%s",
            "%s",
            (op, args) -> "(" + String.join(" ", args) + " " + op + ")"
    );

    protected PostfixAtanExpTest(final int mode) {
        super(mode, new Language(ObjectExpressionTest.ARITHMETIC_OBJECT, POSTFIX, new ArithmeticTests()), "postfix");
    }

    @Override
    protected void testParsing() {
        printParsingError("Empty input", "");
        printParsingError("Unknown variable", "a");
        printParsingError("Invalid number", "-a");
        printParsingError("Missing )", "(z (x y +) *");
        printParsingError("Unknown operation", "( x y @@)");
        printParsingError("Excessive info", "(x y +) x");
        printParsingError("Empty op", "()");
        printParsingError("Invalid unary (0 args)", "(negate)");
        printParsingError("Invalid unary (2 args)", "(x y negate)");
        printParsingError("Invalid binary (0 args)", "(+)");
        printParsingError("Invalid binary (1 args)", "(x +)");
        printParsingError("Invalid binary (3 args)", "(x y z +)");
    }

    @Override
    protected String parse(final String expression) {
        return "parsePostfix('" + expression + "')";
    }

    private void printParsingError(final String description, final String input) {
        final String message = assertParsingError(input, "", "");
        final int index = message.lastIndexOf("in <eval>");
        System.out.format("%-25s: %s\n", description, message.substring(0, index > 0 ? index : message.length()));
    }

    public static void main(final String... args) {
        PrefixAtanExpTest.main(args);
        new PostfixAtanExpTest(BaseJavascriptTest.mode(args, PostfixAtanExpTest.class, "easy", "hard")).run();
    }
}
