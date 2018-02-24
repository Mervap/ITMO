#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#define int long long

using namespace std;

int gcd_fun(int a, int b){
    if(b == 0){
        return a;
    } else{
        return gcd_fun(b, a % b);
    }
}

int_fast32_t main()
{
    freopen("perm.in", "r", stdin);
    freopen("perm.out", "w", stdout);

    int n, k, m;
    cin >> n >> m >> k;

    vector<int> a(n);
    for(int i = 0; i < n; ++i){
        cin >> a[i];
    }

    sort(a.begin(), a.end());

    vector< vector<int> > gcd;
    gcd.assign(n, vector<int> (n));
    for(int i = 0; i < n; ++i){
        for(int j = 0; j < n; ++j){
            gcd[i][j] = gcd_fun(a[i], a[j]);
        }
    }

    int l = (1 << n);

    vector< vector<int> > dp;
    dp.assign(l, vector<int> (n));

    for(int i = 0; i < n; ++i){
        dp[(1 << i)][i] = 1;
    }

    for(int i = 0; i < l; ++i){
        for(int j1 = 0; j1 < n; ++j1){
            if( (i & (1 << j1)) != 0){
                for(int j2 = 0; j2 < n; ++j2){
                    if( (j2 != j1) && ((i & (1 << j2)) != 0) && (gcd[j1][j2] >= k) ){
                        dp[i][j1] += dp[i - (1 << j1)][j2];
                    }
                }
            }
        }
    }

    vector<int> ans;
    int all = l - 1;

    int i = 0;
    while(i < n && m > dp[l - 1][i]){
        m -= dp[l-1][i];
        ++i;
    }

    if(i == n){
        cout << -1;
        return 0;
    }

    ans.push_back(i);
    all -= (1 << i);
    for(int j = 1; j < n; ++j){

        int i = 0;
        while(true){
            if(((all & (1 << i)) != 0) && (gcd[ans[j-1]][i] >= k)){
                if(dp[all][i] >= m){
                    break;
                } else{
                    m -= dp[all][i];
                }
            }
            ++i;
        }

        ans.push_back(i);
        all -= (1 << i);

    }

    for(int i = 0; i < (int)ans.size(); ++i){
        cout << a[ans[i]] << " ";
    }
    return 0;
}
