"use strict";

chapter("Functions");
section("dump");

var dump = function() {
    for (var i = 0; i < arguments.length; i++) {
        println(arguments[i]);
    }
};
println("dump =", dump);
dump(1, 2, "hello", null, undefined);


section("sum");

var sum = function() {
    var result = 0;
    for (var i = 0; i < arguments.length; i++) {
        result += arguments[i];
    }
    return result;
};
println("sum =", sum);
example("sum(1, 2, 3)");


section("minimum");

var minimum = function() {
    var result = Infinity;
    for (var i = 0; i < arguments.length; i++) {
        if (result > arguments[i]) {
            result = arguments[i];
        }
    }
    return result;
};
println("minimum =", minimum);
example("minimum(1, -2, 3)");


section("Named functions and arguments");

function min(a, b) {
    return a < b ? a : b;
}
example("min");
example("min(1, -1)");
var m = min;
example("m(1, -1)");
