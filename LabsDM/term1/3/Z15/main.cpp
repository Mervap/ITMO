#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{
    freopen("num2choose.in", "r", stdin);
    freopen("num2choose.out", "w", stdout);


    int n, k, m;
    cin >> n >> k >> m;

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

    //cout << d[1][1] << "\n";
    //cout << d[2][1] << " " << d[2][2] << "\n";
    //cout << d[3][1] << " " << d[3][2] << " " << d[3][3] << "\n";
    //cout << d[4][1] << " " << d[4][2] << " " << d[4][3] << " " << d[4][4] << "\n";

    int j = 1;
    for(int i = k - 1; i >= 0; --i){
        while(m - d[n - j][i] >= 0 && d[n - k + i][i] != 0){
            m -= d[n - j][i];
            ++j;
        }
        cout << j << " ";
        ++j;
    }

    return 0;
}
