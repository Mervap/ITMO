#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;
vector< vector<int> > dp;
vector<int> a, c;
int n, m;

void out(int i, int w, int lvl){
    if(i == 0){
        cout << lvl << "\n";
        return;
    }

    if(dp[i-1][w] == dp[i][w]){
        out(i-1, w, lvl);
    } else{
        if(w - a[i] >= 0){
            out(i-1, w - a[i], lvl + 1);
        }
        cout << i << " ";
    }
}

int main()
{
    freopen("knapsack.in", "r", stdin);
    freopen("knapsack.out", "w", stdout);

    cin >> n >> m;
    a.assign(n + 1, 0);
    c.assign(n + 1, 0);
    for(int i = 1; i <= n; ++i){
        cin >> a[i];
    }

    for(int i = 1; i <= n; ++i){
        cin >> c[i];
    }

    dp.assign(n+1, vector<int>(m+1));

    for(int i = 1; i <= n; ++i){
        for(int j = 1; j <= m; ++j){
            dp[i][j] = dp[i-1][j];
            if(j - a[i] >= 0){
                dp[i][j] = max(dp[i][j], dp[i-1][j - a[i]] + c[i]);
            }
        }
    }

    out(n, m, 0);

    return 0;
}
