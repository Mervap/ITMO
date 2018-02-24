
#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{
    freopen("choose2num.in", "r", stdin);
    freopen("choose2num.out", "w", stdout);


    int n, k, m;
    cin >> n >> k;

    int cnt = 0;
    vector< vector<int> > d;
    d.assign(n+1, vector<int> (n+1));

    d[0][0] = 1;
    for(int i = 1; i <= n; ++i){
        d[i][0] = 1;
        for(int j = 1; j <= n; ++j){
            d[i][j] = d[i-1][j-1] + d[i-1][j];
        }
    }

    vector<int> a(k+1);
    for(int i = 1; i <= k; ++i){
        cin >> a[i];
    }

    sort(a.begin(), a.end());

    int ans = 0;
    for(int i = 1; i <= k; ++i){
        for(int j = a[i-1] + 1; j < a[i]; ++j){
            ans += d[n-j][k-i];
        }
    }

    cout << ans;

    return 0;
}
