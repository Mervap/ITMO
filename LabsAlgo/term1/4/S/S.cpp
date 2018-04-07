#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

struct mat {
    long long ff, fs, sf, ss;
};

mat base;
long long md = 999999937;

mat mul(mat a, mat b) {
    mat c;
    c.ff = ((a.ff * b.ff) % md + (a.fs * b.sf) % md) % md;
    c.fs = ((a.fs * b.ff) % md + (a.ss * b.sf) % md) % md;
    c.sf = c.fs;
    c.ss = ((a.fs * b.sf) % md + (a.ss * b.ss) % md) % md;
    return c;
}

mat fib(long long n, mat x) {
    if (n == 1) {
        return x;
    }
    if (n % 2 == 0) {
        mat x1 = fib(n / 2, x);
        return mul(x1, x1);
    } else {
        mat x1 = fib((n - 1) / 2, x);
        return mul(base, mul(x1, x1));
    }
}

int main() {

    freopen("sequences.in", "r", stdin);
    freopen("sequences.out", "w", stdout);

    long long n;
    while (cin >> n && n != 0) {
        n = 5 + 3 * (n - 1);

        base.ff = 1;
        base.fs = 1;
        base.sf = 1;
        base.ss = 0;

        mat x;
        x = fib(n, base);
        cout << x.fs << "\n";
    }

    return 0;
}
