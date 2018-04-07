#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

const long long lim = 100000000000000000;
vector<vector<long long> > r;
vector<vector<long long> > dp;
int n;

long long ans(int i, long long mask) {

    if (dp[i][mask] != lim) {
        return dp[i][mask];
    }

    for (int j = 0; j < n; ++j) {
        if (r[i][j] != 0 && ((1ll << j) & mask) != 0) {
            dp[i][mask] = min(dp[i][mask], ans(j, mask ^ (1ll << j)) + r[i][j]);
        }
    }

    return dp[i][mask];
}

int main() {

    freopen("salesman.in", "r", stdin);
    freopen("salesman.out", "w", stdout);

    int m;
    cin >> n >> m;

    r.assign(n, vector<long long>(n));

    int a, b;
    long long w;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> w;
        --a;
        --b;
        r[a][b] = w;
        r[b][a] = w;
    }

    vector<long long> c;
    c.assign((1ll << n), lim);
    dp.assign(n, c);

    for (int i = 0; i < n; ++i) {
        dp[i][0] = 0;
    }

    long long answer = lim;
    int tec = 0;
    for (int i = 0; i < n; ++i) {
        long long t = ans(i, (1ll << n) - 1 - (1ll << i));
        if (t < answer) {
            answer = t;
            tec = i;
        }
    }

    if (answer == lim) {
        cout << -1;
    } else {
        cout << answer << "\n";
    }

    long long mask = (1ll << n) - 1 - (1ll << tec);
    cout << tec + 1 << " ";
    int i = tec;
    for (int q = 1; q < n; ++q) {
        for (int j = 0; j < n; ++j) {
            if (r[i][j] != 0 && ((1ll << j) & mask) != 0) {
                if (dp[j][mask ^ (1ll << j)] + r[i][j] == dp[i][mask]) {
                    cout << j + 1 << " ";
                    i = j;
                    mask = (mask ^ (1ll << j));
                    break;
                }
            }
        }
    }

    return 0;
}
