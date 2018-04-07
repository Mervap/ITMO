#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;
vector<int> p;
vector<vector<unsigned long long> > dp;
vector<vector<int> > zn;

int n;


int cnt(int l, int r) {
    if (zn[l][r] != -1) {
        return zn[l][r];
    }

    if (r - l == 1) {
        dp[l][r] = 0;
        zn[l][r] = r;
    } else {
        for (int i = l + 1; i < r; ++i) {
            cnt(l, i);
            cnt(i, r);
            unsigned long long m = p[l - 1] * p[i - 1] * p[r - 1] + dp[l][i] + dp[i][r];
            if (m < dp[l][r]) {
                dp[l][r] = m;
                zn[l][r] = i;
            }
        }
    }
    return zn[l][r];
}

string ans(int l, int r) {
    if (r - l == 1) {
        return "A";
    }
    if (r == l) {
        return "";
    }
    int k = cnt(l, r);
    return ("(" + ans(l, k) + ans(k, r) + ")");
}

int main() {

    freopen("matrix.in", "r", stdin);
    freopen("matrix.out", "w", stdout);

    cin >> n;
    p.assign(n + 1, 0);

    vector<unsigned long long> c;
    unsigned long long m = (1ull << 63);
    c.assign(n + 2, m);
    dp.assign(n + 2, c);

    vector<int> d;
    d.assign(n + 2, -1);
    zn.assign(n + 2, d);

    int a, b;
    for (int i = 0; i < n; ++i) {
        cin >> a >> b;
        p[i] = a;
    }
    p[n] = b;

    cout << ans(1, n + 1) << endl;
    return 0;
}
