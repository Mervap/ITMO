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

int main() {
    int n, m;

    cin >> n >> m;
    int len = std::max(n, m);
    vector<long long> p(n + 1), q(m + 1);

    for (int i = 0; i <= n; ++i) {
        cin >> p[i];
    }

    for (int i = 0; i <= m; ++i) {
        cin >> q[i];
    }

    cout << len << endl;
    for (int i = 0; i <= len; ++i) {
        cout << (getReal(i, p) + getReal(i, q)) % MOD << " ";
    }
    cout << endl;

    vector<long long> mul(n + m + 3);
    for (int i = 0; i <= n + m + 2; ++i) {
        for (int j = 0; j <= i; ++j) {
            mul[i] += (getReal(j, p) * getReal(i - j, q)) % MOD;
            mul[i] %= MOD;
        }
    }

    int st = n + m + 2;
    while (st > 0 && mul[st] == 0) {
        --st;
    }

    cout << st << endl;
    for (int i = 0; i <= st; ++i) {
        cout << mul[i] << " ";
    }
    cout << endl;

    vector<long long> div(1000);

    for (int i = 0; i < 1000; ++i) {
        div[i] = getReal(i, p);
        for (int j = 0; j < i; ++j) {
            div[i] -= (div[j] * getReal(i - j, q)) % MOD;
            if (div[i] < 0) {
                div[i] += MOD;
            }
        }
        div[i] /= getReal(0, q);
        cout << div[i] << " ";
    }

    return 0;
}