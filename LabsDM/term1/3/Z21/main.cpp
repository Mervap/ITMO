
#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{
    freopen("num2part.in", "r", stdin);
    freopen("num2part.out", "w", stdout);


    long long n, k;
    cin >> n >> k;


    vector< vector<long long> > d;
    d.assign(n+1, vector<long long> (n+1));

    for(int i = 0; i <= n; ++i){
        d[i][i] = 1;
        for(int j = i-1; j >= 0; --j){
            if(i - j > 0){
                d[i][j] += d[i-j][j];
            }
            if(j < n){
                d[i][j] += d[i][j+1];
            }
        }
    }

    /*
    1 1 1 1
    1 1 2
    1 3
    2 2
    4
    1 - 5
    2 - 2
    3 - 1
    4 - 1
    */
    int i = 1;
    //cout << d[4][1] << "i";
    vector<int> ans;
    while(n > 0){
        while(i < n && d[n][i] - d[n][i+1] <= k){
            k -= d[n][i] - d[n][i+1];
            ++i;
        }
        ans.push_back(i);
        n -= i;
    }

    for(int i = 0; i < (int)ans.size() -1; ++i){
        cout << ans[i] << "+";
    }
    cout << ans[(int)ans.size()-1];

    return 0;
}
