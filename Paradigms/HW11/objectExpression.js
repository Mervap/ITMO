var expression = (function () {

    // New-operator
    function newOperation(constructor, args) {
        var tmp = Object.create(constructor.prototype);
        constructor.apply(tmp, args);
        return tmp;
    }

    // Const
    function Const(x) {
        this.getValue = function () {
            return x;
        }
    }

    Const.prototype.evaluate = function () {
        return this.getValue();
    };
    Const.prototype.toString = function () {
        return this.getValue().toString();
    };
    Const.prototype.prefix = Const.prototype.toString;
    Const.prototype.postfix = Const.prototype.toString;

    var ZERO = new Const(0);
    var ONE = new Const(1);

    Const.prototype.diff = function () {
        return ZERO;
    };

    // Variable
    var VAR_NAMES = {
        "x": 0,
        "y": 1,
        "z": 2
    };

    function Variable(name) {
        this.getName = function () {
            return name;
        };
        this.getId = function () {
            return VAR_NAMES[name];
        }
    }

    Variable.prototype.evaluate = function () {
        return arguments[this.getId()];
    };
    Variable.prototype.toString = function () {
        return this.getName();
    };
    Variable.prototype.prefix = Variable.prototype.toString;
    Variable.prototype.postfix = Variable.prototype.toString;
    Variable.prototype.diff = function (v) {
        if (v === this.getName()) {
            return ONE;
        } else {
            return ZERO;
        }
    };

    // Prototype for prototype for operations
    var OperationProt = {

        evaluate: function () {
            var args = arguments;
            var res = this.getOp().map(function (value) {
                return value.evaluate.apply(value, args);
            });
            return this.operate.apply(null, res);
        },

        toString: function () {
            return this.getOp().join(" ") + " " + this.getSymbol();
        },

        diff: function (v) {
            var args = this.getOp();
            args = args.concat(args.map(function (value) {
                return value.diff(v);
            }));
            return this.doDiff.apply(this, args);
        },
        prefix: function () {
            return "(" + this.getSymbol() + " " + this.getOperands().map(function (value) {
                return value.prefix()
            }).join(" ") + ")";
        },
        postfix: function () {
            return "(" + this.getOperands().map(function (value) {
                return value.postfix()
            }).join(" ") + " " + this.getSymbol() + ")";
        }
    };

    // Prototype for operations
    function Operation() {
        var operands = [].slice.call(arguments);
        this.getOp = function () {
            return operands;
        }
    }

    Operation.prototype = Object.create(OperationProt);

    // Constructor? Abstract? What is it?
    function MakeOperation(maker, symbol, operate, howDiff) {
        this.constructor = maker;
        this.operate = operate;
        this.doDiff = howDiff;
        this.getSymbol = function () {
            return symbol;
        };
    }

    MakeOperation.prototype = Operation.prototype;

    function operationFactory(symbol, operate, howDiff) {
        var result = function () {
            var args = arguments;
            Operation.apply(this, args);
        };
        result.prototype = new MakeOperation(result, symbol, operate, howDiff);
        return result;
    }

    // Some helpful function
    function isGood(a) {
        return (a instanceof Const || a instanceof Variable || a instanceof Operation);
    }

    //Add
    var Add = operationFactory(
        "+",
        function (a, b) {
            return a + b;
        },
        function (a, b, da, db) {
            return new Add(da, db);
        }
    );

    // Subtract
    var Subtract = operationFactory(
        "-",
        function (a, b) {
            return a - b;
        },
        function (a, b, da, db) {
            return new Subtract(da, db);
        }
    );

    // Multiply
    var Multiply = operationFactory(
        "*",
        function (a, b) {
            return a * b;
        },
        function (a, b, da, db) {
            return new Add(new Multiply(da, b), new Multiply(a, db));
        }
    );

    // Divide
    var Divide = operationFactory(
        "/",
        function (a, b) {
            return a / b;
        },
        function (a, b, da, db) {
            return new Divide(new Subtract(new Multiply(da, b), new Multiply(a, db)), new Multiply(b, b));
        }
    );

    // Negate
    var Negate = operationFactory(
        "negate",
        function (a) {
            return -a;
        },
        function (a, da) {
            return new Negate(da);
        }
    );

    // Parser
    var OPERATIONS = {
        "+": Add,
        "-": Subtract,
        "*": Multiply,
        "/": Divide,
        "negate": Negate
    };
    var CNT_ARGUMENTS = {
        "+": 2,
        "-": 2,
        "*": 2,
        "/": 2,
        "negate": 1,
        "square": 1,
        "sqrt": 1
    };

    var parse = function (expression) {
        var tokens = expression.split(" ").filter(function (t) {
            return t.length > 0;
        });

        var stack = [];
        for (var i = 0; i < tokens.length; ++i) {
            if (tokens[i] in OPERATIONS) {
                var arg = [];
                for (var j = 0; j < CNT_ARGUMENTS[tokens[i]]; ++j) {
                    arg.push(stack.pop());
                }

                arg.reverse();
                stack.push(newOperation(OPERATIONS[tokens[i]], arg));
            } else if (tokens[i] in VAR_NAMES) {
                stack.push(new Variable(tokens[i]));
            } else {
                stack.push(new Const(parseInt(tokens[i])));
            }
        }
        return stack.pop();
    };

    // Return
    return {
        "Const": Const,
        "Variable": Variable,
        "Add": Add,
        "Subtract": Subtract,
        "Multiply": Multiply,
        "Divide": Divide,
        "Negate": Negate,
        "Square": Square,
        "Sqrt": Sqrt,
        "parse": parse
    }
})();

for (var f in expression) {
    global[f] = expression[f];
}

var expr = parse('x 2 +');
//println(expr);