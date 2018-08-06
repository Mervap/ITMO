var operation = function (op) {
    return function () {
        var operations = arguments;
        return function () {
            var res = [];
            for (var i = 0; i < operations.length; i++) {
                res.push(operations[i].apply(null, arguments));
            }
            return op.apply(null, res);
        }
    }
};

var MATH_CONSTS = {
    "pi": Math.PI,
    "e": Math.E
};

var VAR_NAMES = {
    "x": 0,
    "y": 1,
    "z": 2
};

var variable = function (name) {
    return function () {
        return arguments[VAR_NAMES[name]];
    }
};

for (var v in VAR_NAMES) {
    this[v] = variable(v);
}

var cnst = function (value) {
    return function () {
        return value;
    };
};

for (var cn in MATH_CONSTS) {
    this[cn] = cnst(MATH_CONSTS[cn]);
}

var add = operation(function (a, b) {
    return a + b;
});

var subtract = operation(function (a, b) {
    return a - b;
});

var multiply = operation(function (a, b) {
    return a * b;
});

var divide = operation(function (a, b) {
    return a / b;
});

var negate = operation(function (a) {
    return -a;
});

var min3 = operation(function () {
    return Math.min.apply(null, arguments);
});

var max5 = operation(function () {
    return Math.max.apply(null, arguments);
});

var parse = function (expression) {

    var OPERATIONS = {
        "+": add,
        "-": subtract,
        "*": multiply,
        "/": divide,
        "negate": negate,
        "min3": min3,
        "max5": max5
    };

    var CNT_ARGUMENTS = {
        "+": 2,
        "-": 2,
        "*": 2,
        "/": 2,
        "negate": 1,
        "min3": 3,
        "max5": 5
    };

    var tokens = expression.split(" ").filter(function (t) {
        return t.length > 0;
    });

    var stack = [];
    for (var i = 0; i < tokens.length; ++i) {
        if (tokens[i] in MATH_CONSTS) {
            stack.push(cnst(MATH_CONSTS[tokens[i]]));
        } else if (tokens[i] in OPERATIONS) {
            var arg = [];
            for (var j = 0; j < CNT_ARGUMENTS[tokens[i]]; ++j) {
                arg.push(stack.pop());
            }

            arg.reverse();
            stack.push(OPERATIONS[tokens[i]].apply(null, arg));
        } else if (tokens[i] in VAR_NAMES) {
            stack.push(variable(tokens[i]));
        } else {
            stack.push(cnst(parseInt(tokens[i])));
        }
    }
    return stack.pop();
};

//println(add(pi, x)(5));