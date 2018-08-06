"use strict";

chapter("Closures");
section("Functions with state");

function incrementor(step) {
    var n = 0;
    return function() {
        n += step;
        return n;
    }
}

var inc = incrementor(10);
println("n and step are captured in closure");
println("    inc() = " + inc());
println("    inc() = " + inc());
println("    inc() = " + inc());


section("Be careful");

println("adders1 shares same i:");
function adders1(n) {
    var a = [];
    for (var i = 0; i < n; i++) {
        a.push(function(v) { return i + v; });
    }
    return a;
}

a = adders1(3);
for (j = 0; j < a.length; j++) {
    println("    adders1[" + j + "] adds " + a[j](0));
}


section("Intermediate function trick");

println("adders2 has a copy of i named w:");
function adders2(n) {
    var a = [];
    for (var i = 0; i < n; i++) {
        a.push(
            (function(w) {
                return function(v) { return w + v; }
            })(i) // Call of declared intermediate function
        );
    }
    return a;
}

var a = adders2(3);
for (var j = 0; j < a.length; j++) {
    println("    adders2[" + j + "] adds " + a[j](0));
}
println();


section("Common shared state");
function PrivatePoint(x, y) {
    println("Constructor called");
    var z = 0;
    this.getX = function() { return x; };
    this.setX = function(value) { x = value; };
    this.getY = function() { return y; };
    this.setY = function(value) { y = value; };
    this.getZ = function() { return z; };
    this.setZ = function(value) { z = value; };
}

var privatePoint = new PrivatePoint(10, 20);
dumpObject("privatePoint", privatePoint);

privatePoint.setX(100);
privatePoint.setZ(1000);
dumpObject("Modified privatePoint", privatePoint);
