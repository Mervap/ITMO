#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#define int long long

using namespace std;

const int mod = 1000000007;

struct ver{
    int to;
    int mn;
    int mx;
};

struct reb{
    int kol;
    int mn;
    int mx;
};

vector< vector<ver> > gr;
vector<reb> ans;
vector<int> kol;
int n, m, l = 0;

int dfs(int i, int p){

    ver val;
    val.mn = -1;
    val.mx = -1;
    for(auto j : gr[i]){
        if(j.to == p){
            val = j;
            continue;
        }
        kol[i] += dfs(j.to, i);
    }

    if(i != 1){
        reb a;
        a.kol = kol[i]*(n - kol[i]);
        a.mn = val.mn;
        a.mx = val.mx;
        ans.push_back(a);
    }

    return kol[i];
}

int32_t main()
{
    //freopen("input.txt", "r", stdin);
    scanf("%lld%lld", &n, &m);

    if(m % 2 == 1){
        cout << 0;
        return 0;
    }

    m /= 2;

    if( (n - 1) * (n - 1) > m) {
        cout << 0;
        return 0;
    }

    gr.assign(n + 1, vector<ver>(0));
    kol.assign(n + 1, 1);

    int a,b;
    ver v;
    for(int i = 0; i < n - 1; ++i){
        scanf("%lld%lld%lld%lld", &a, &b, &v.mn, &v.mx);
        v.to = b;
        gr[a].push_back(v);
        v.to = a;
        gr[b].push_back(v);
    }

    dfs(1, -1);

    vector<int> dp(m + 1);
    dp[0] = 1;

    for(int i = 0; i < n - 1; ++i){
        m -= ans[i].kol*ans[i].mn;
        ans[i].mx -= ans[i].mn;
        ans[i].mn = 0;
        if(m < 0){
            cout << "0";
            return 0;
        }
    }

    for(int i = 0; i < n - 1; i++) {
        for (int j = ans[i].kol; j <= m; j++) {
            dp[j] = (dp[j] + dp[j - ans[i].kol]) % mod;
        }

        int z = (ans[i].mx + 1) * ans[i].kol;
        if(z <= m) {
            for(int j = m; j >= z; j--) {
                dp[j] = (dp[j] + mod - dp[j - z]) % mod;
            }
        }
    }

    cout << dp[m];




    return 0;
}
