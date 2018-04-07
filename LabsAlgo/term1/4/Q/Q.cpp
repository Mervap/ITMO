#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

#define n nnnn

using namespace std;

vector<vector<int> > dp, dp1, dp2;
int l, m;
int p;
string n = "";

void divide() {
    string tmp = "", nw = "";
    for (int i = 0; i < (int) n.length(); ++i) {
        tmp = tmp + n[i];
        if (tmp.length() == 1) {
            nw += to_string((tmp[0] - '0') / 2);
            if (tmp[0] % 2 == 1) {
                tmp = "1";
            } else {
                tmp = "";
            }
        }
        if (tmp.length() == 2) {
            nw += to_string(5 + (tmp[1] - '0') / 2);
            if (tmp[1] % 2 == 1) {
                tmp = "1";
            } else {
                tmp = "";
            }
        }
    }
    int i = 0;
    n = "";
    while (i < (int) nw.length() - 1 && nw[i] == '0') {
        ++i;
    }
    while (i < (int) nw.length()) {
        n += nw[i];
        ++i;
    }
}

void alg() {
    if (n == "1") {
        return;
    }
    if (n == "2") {
        dp1.assign(l, vector<int>(l, 0));
        return;
    }

    bool b3 = (n == "3");
    bool bch = ((n[(int) n.length() - 1] - '0') % 2 == 1);

    divide();
    alg();

    dp1.assign(l, vector<int>(l, 0));
    for (int i = 0; i < l; ++i) {
        for (int j = 0; j < l; ++j) {
            for (int k = 0; k < l; ++k) {
                dp1[i][j] = (dp1[i][j] + dp[i][k] * dp[k][j]) % p;
            }
        }
    }

    dp = dp1;

    if (b3) {
        return;
    }

    for (int rr = 0; rr < 1; ++rr) {
        dp1.assign(l, vector<int>(l, 0));
        for (int i = 0; i < l; ++i) {
            for (int j = 0; j < l; ++j) {
                for (int k = 0; k < l; ++k) {
                    dp1[i][j] = (dp1[i][j] + dp[i][k] * dp2[k][j]) % p;
                }
            }
        }
        dp = dp1;
    }

    if (bch) {
        dp1.assign(l, vector<int>(l, 0));
        for (int i = 0; i < l; ++i) {
            for (int j = 0; j < l; ++j) {
                for (int k = 0; k < l; ++k) {
                    dp1[i][j] = (dp1[i][j] + dp[i][k] * dp2[k][j]) % p;
                }
            }
        }
        dp = dp1;
    }

    return;
}

int main() {
    freopen("nice3.in", "r", stdin);
    freopen("nice3.out", "w", stdout);

    cin >> n >> m >> p;

    l = (1 << m);

    dp.assign(l, vector<int>(l));
    dp1 = dp;

    for (int i = 0; i < l; ++i) {
        for (int j = 0; j < l; ++j) {
            int f = 1;
            for (int k = 1; k < m; ++k) {
                if ((((i & (1 << k)) != 0) && ((i & (1 << (k - 1))) != 0) && ((j & (1 << k)) != 0) &&
                     ((j & (1 << (k - 1))) != 0))
                    || (((i & (1 << k)) == 0) && ((i & (1 << (k - 1))) == 0) && ((j & (1 << k)) == 0) &&
                        ((j & (1 << (k - 1))) == 0))) {
                    f = 0;
                    break;
                }
            }
            dp[i][j] = f;
        }
    }
    dp2 = dp;

    if (n == "1") {
        cout << l % p;
        return 0;
    }
    alg();


    vector<int> ans(l);
    for (int i = 0; i < l; ++i) {
        for (int j = 0; j < l; ++j) {
            ans[j] = (ans[j] + dp[i][j]) % p;
        }
    }
    int z = 0;
    for (int i = 0; i < l; ++i) {
        z = (z + ans[i]) % p;
    }

    cout << z;
    return 0;
}
