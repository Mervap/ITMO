#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#define int unsigned long long

using namespace std;

vector< vector<int> > dp, t;

void out(int l, int r, string s){
    if(l == r){
        cout << s << "\n";
        return;
    }
    int k = t[l][r];
    out(l, k, s + "0");
    out(k + 1, r, s + "1");
}

int_fast32_t main()
{
    freopen("optimalcode.in", "r", stdin);
    freopen("optimalcode.out", "w", stdout);

    int n;
    cin >> n;

    vector<int> ff(n);
    for(int i = 0; i < n; ++i){
        cin >> ff[i];
    }

    vector< vector<int> > f;
    f.assign(n, vector<int> (n));

    for(int i = 0; i < n; ++i){
        f[i][i] = ff[i];
        for(int j = i + 1; j < n; ++j){
            f[i][j] = f[i][j - 1] + ff[j];
        }
    }

    dp.assign(n + 1, vector<int> (n + 1, 2100000000));
    t.assign(n + 1, vector<int> (n + 1));

    for(int i = 0; i < n; ++i){
        t[i][i] = i;
        dp[i][i] = 0;
    }

    int l, r;
    for(int i = 1; i < n; ++i){
        for(int j = 0; j < n - i; ++j){
            l = j;
            r = j + i;
            t[l][r] = t[l][r - 1];
            dp[l][r] = dp[l][t[l][r]] + dp[t[l][r] + 1][r];
            for(int k = t[l][r - 1] + 1; k <= t[l + 1][r]; ++k){
                if(dp[l][k] + dp[k + 1][r] <= dp[l][r]){
                    dp[l][r] = dp[l][k] + dp[k + 1][r];
                    t[l][r] = k ;
                }
            }
            dp[l][r] += f[l][r];        }
    }

    cout << dp[0][n - 1] << "\n";
    out(0, n - 1, "");
    return 0;
}
