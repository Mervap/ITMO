#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

const long long MOD = 998'244'353;

long long getReal(int ind, vector<long long> &v) {
    if (ind >= v.size()) {
        return 0;
    }

    return v[ind];
}

long long binMul(long long a, int n) {
    if (n == 1) {
        return a;
    }

    if (n % 2 == 1) {
        return (a * binMul(a, n - 1)) % MOD;
    } else {
        long long tmp = binMul(a, n / 2);
        return (tmp * tmp) % MOD;
    }
}

long long getReverse(long long a) {
    return binMul(a, MOD - 2);
}

vector<long long> mulPol(vector<long long> &p, vector<long long> &q, int limit) {
    int n = p.size();
    int m = q.size();

    int size = std::min(n + m + 3, limit);
    vector<long long> mul(size);
    for (int i = 0; i < size; ++i) {
        for (int j = 0; j <= i; ++j) {
            mul[i] += (getReal(j, p) * getReal(i - j, q)) % MOD;
            mul[i] %= MOD;
        }
    }

    int st = size - 1;
    while (st > 0 && mul[st] == 0) {
        --st;
    }

    mul.resize(st + 1);
    return mul;
}

long long binCoef(long long n) {
    long long a = 1;
    long long b = 1;
    --n;
    for (int i = 0; i <= n; ++i) {
        a *= (1 - 2 * i + MOD);
        a %= MOD;
        b *= ((i + 1) * 2) % MOD;
        b %= MOD;
    }

    return (a * getReverse(b)) % MOD;
}

int main() {
    int n, m;

    cin >> n >> m;
    vector<long long> p(n + 1);

    for (int i = 0; i <= n; ++i) {
        cin >> p[i];
    }

    vector<long long> sqrt(m), exp(m), ln(m);
    sqrt[0] = exp[0] = 1;
    vector<long long> tmp = {1};
    long long fact = 1;
    long long lnCoef = -1 + MOD;
    for (int i = 1; i < m; ++i) {
        tmp = mulPol(tmp, p, m);

        auto bCoef = binCoef(i);
        if (bCoef < 0) {
            bCoef += MOD;
        }

        fact *= i;
        fact %= MOD;

        lnCoef *= -1;
        lnCoef += MOD;
        for (int j = 0; j < m; ++j) {
            sqrt[j] += (bCoef * getReal(j, tmp)) % MOD;
            sqrt[j] %= MOD;

            exp[j] += ((1 * getReverse(fact)) % MOD * getReal(j, tmp)) % MOD;
            exp[j] %= MOD;

            ln[j] += ((lnCoef * getReverse(i)) % MOD * getReal(j, tmp)) % MOD;
            ln[j] %= MOD;
        }
    }

    for (auto e : sqrt) {
        cout << e << " ";
    }
    cout << endl;

    for (auto e : exp) {
        cout << e << " ";
    }
    cout << endl;

    for (auto e : ln) {
        cout << e << " ";
    }
    cout << endl;
    return 0;
}