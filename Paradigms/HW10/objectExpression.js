var expression = (function () {

    // New-operator
    function newOperation(constructor, args) {
        var tmp = Object.create(constructor.prototype);
        constructor.apply(tmp, args);
        return tmp;
    }

    // Prototype for Const & Variable
    var ConstVarPrototype = {
        simplify: function () {
            return this;
        }
    };

    // Const
    function Const(x) {
        this.getValue = x;
    }

    Const.prototype = Object.create(ConstVarPrototype);
    Const.prototype.evaluate = function () {
        return this.getValue;
    };
    Const.prototype.toString = function () {
        return String(this.getValue);
    };

    var ZERO = new Const(0);
    var ONE = new Const(1);
    var TWO = new Const(2);

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
        this.getName = name;
        this.getId = VAR_NAMES[name];
    }

    Variable.prototype = Object.create(ConstVarPrototype);
    Variable.prototype.evaluate = function () {
        return arguments[this.getId];
    };
    Variable.prototype.toString = function () {
        return this.getName;
    };
    Variable.prototype.diff = function (v) {
        if (v === this.getName) {
            return ONE;
        } else {
            return ZERO;
        }
    };

    // Prototype for prototype for operations
    var OperationPrototype = {

        evaluate: function () {
            var args = arguments;
            var res = this.getOp().map(function (x) {
                return x.evaluate.apply(x, args);
            });
            return this.operate.apply(null, res);
        },

        toString: function () {
            return this.getOp().join(" ") + " " + this.getSymbol();
        },

        diff: function (v) {
            var args = this.getOp();
            args = args.concat(args.map(function (x) {
                return x.diff(v);
            }));
            return this.doDiff.apply(this, args);
        },

        simplify: function () {
            var args = this.getOp().map(function (x) {
                return x.simplify();
            });
            var f;
            for (var i = 0; i < args.length; ++i) {
                if (!(args[i] instanceof Const)) {
                    f = true;
                    break;
                }
            }

            var res = newOperation(this.constructor, args);
            if (!f) {
                return new Const(res.evaluate());
            }
            if (this.doSimplify !== undefined) {
                return this.doSimplify.apply(this, args);
            }
            return res;
        }
    };

    // Prototype for operations
    function Operation() {
        var operands = [].slice.call(arguments);
        this.getOp = function () {
            return operands;
        }
    }
    Operation.prototype = Object.create(OperationPrototype);

    function MakeOperation(maker, symbol, operate, howDiff, howSimp) {
        this.constructor = maker;
        this.operate = operate;
        this.doDiff = howDiff;
        this.doSimplify = howSimp;
        this.getSymbol = function () {
            return symbol;
        };
    }

    MakeOperation.prototype = Operation.prototype;

    function operationFactory(symbol, operate, howDiff, howSimp) {
        var result = function () {
            var args = arguments;
            Operation.apply(this, args);
        };
        result.prototype = new MakeOperation(result, symbol, operate, howDiff, howSimp);
        return result;
    }

    // Some helpful function
    function isZero(a) {
        return ((a instanceof Const) && (a.getValue === 0));
    }

    function isOne(a) {
        return ((a instanceof Const) && (a.getValue === 1));
    }

    //Add
    var Add = operationFactory(
        "+",
        function (a, b) {
            return a + b;
        },
        function (a, b, da, db) {
            return new Add(da, db);
        },
        function (a, b) {
            return isZero(a) ? b
                : isZero(b) ? a
                : new Add(a, b);
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
        },
        function (a, b) {
            if (isZero(b)) {
                return a;
            }
            return new Subtract(a, b);
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
        },
        function (a, b) {
            if (isZero(a) || isZero(b)) {
                return ZERO;
            }
            if (isOne(a)) {
                return b;
            }
            if (isOne(b)) {
                return a;
            }
            return new Multiply(a, b);
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
        },
        function (a, b) {
            if (isZero(a)) {
                return ZERO;
            }
            if (isOne(b)) {
                return a;
            }
            return new Divide(a, b);
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

    // Square
    var Square = operationFactory(
        "square",
        function (a) {
            return a * a;
        },
        function (a, da) {
            return new Multiply(new Multiply(TWO, a), da);
        }
    );

    // Sqrt
    var Sqrt = operationFactory(
        "sqrt",
        function (a) {
            return Math.sqrt(Math.abs(a));
        },
        function (a, da) {
            // sqrt(|x|) = sqrt(sqrt(x^2))
            return new Divide(new Multiply(new Sqrt(new Sqrt(new Square(a))), da), new Multiply(TWO, a));
        }
    );

    // Parser
    var OPERATIONS = {
        "+": Add,
        "-": Subtract,
        "*": Multiply,
        "/": Divide,
        "negate": Negate,
        "square": Square,
        "sqrt": Sqrt
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

//var expr = parse('x 2 +');
//println(expr);

