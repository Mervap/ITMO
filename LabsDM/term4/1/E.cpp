#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

const int64_t MOD = 998'244'353;

int64_t getReal(int ind, vector<int64_t> &v) {
    if (ind >= v.size()) {
        return 0;
    }

    return v[ind];
}

int main() {
    int k, n;

    cin >> k >> n;

    vector<int64_t> l, r;

    vector<vector<int64_t>> binCoef(k + 1, vector<int64_t>(k + 1));

    for (int i = 0; i <= k; ++i) {
        binCoef[i][0] = binCoef[i][i] = 1;
        for (int j = 1; j < i; ++j) {
            binCoef[i][j] = (binCoef[i - 1][j - 1] + binCoef[i - 1][j]) % MOD;
            if (binCoef[i][j] < 0) {
                binCoef[i][j] += MOD;
            }
        }
    }

    for (int i = 0; k - 1 - i - 1 >= i; ++i) {
        l.push_back(binCoef[k - 1 - i - 1][i] * (i % 2 == 0 ? 1 : -1));
    }
    for (int i = 0; k - i - 1 >= i; ++i) {
        r.push_back(binCoef[k - i - 1][i] * (i % 2 == 0 ? 1 : -1));
    }

    vector<int64_t> b_div(n + 1);
    b_div[0] = (1 / getReal(0, r));
    for (int i = 0; i <= n; ++i) {
        for (int j = 0; j < i; ++j) {
            b_div[i] -= (b_div[j] * getReal(i - j, r)) % MOD;
            b_div[i] %= MOD;
            if (b_div[i] < 0) {
                b_div[i] += MOD;
            }
        }
        b_div[i] /= getReal(0, r);
    }

    for (int i = 0; i < n; ++i) {
        int64_t ans = 0;
        for (int j = 0; j <= i; ++j) {
            ans += (getReal(j, l) * getReal(i - j, b_div)) % MOD;
            ans %= MOD;
            if (ans < 0) {
                ans += MOD;
            }
        }

        cout << ans << '\n';
    }
}