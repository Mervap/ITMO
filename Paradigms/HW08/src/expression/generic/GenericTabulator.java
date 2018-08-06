package expression.generic;

import expression.TripleExpression;
import expression.exceptions.*;
import expression.operations.*;
import expression.parser.ExpressionParser;
import expression.parser.Parser;

import java.util.HashMap;

public class GenericTabulator implements Tabulator {

    private final static HashMap <String, Operation <?>> MODES = new HashMap <>();

    static {
        MODES.put("i", new IntegerOperation(true));
        MODES.put("u", new IntegerOperation(false));
        MODES.put("l", new LongOperation());
        MODES.put("s", new ShortOperation());
        MODES.put("bi", new BigIntegerOperation());
        MODES.put("d", new DoubleOperation());
    }

    private Operation <?> operation(String mode) throws UnknownModeException {
        Operation <?> result = MODES.get(mode);
        if (result == null) {
            throw new UnknownModeException(mode);
        }
        return result;
    }

    private <T> Object[][][] answer(Operation <T> op, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        Parser <T> parser = new ExpressionParser <T>(op);
        TripleExpression <T> parse;

        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        try {
            parse = parser.parse(expression);
        } catch (ParsingException e) {
            return result;
        }

        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        result[i - x1][j - y1][k - z1] = parse.evaluate(op.parseNumber(Integer.toString(i)), op.parseNumber(Integer.toString(j)), op.parseNumber(Integer.toString(k)));
                    } catch (ParsingException | EvaluatingException e) {
                        result[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return result;
    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return answer(operation(mode), expression, x1, x2, y1, y2, z1, z2);
    }

}
