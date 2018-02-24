
#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{
    //freopen("brackets2num.in", "r", stdin);
    //freopen("brackets2num.out", "w", stdout);


    unsigned long long n, k;
    string s;
    cin >> s;
    n = s.length()/2;

    vector< vector<unsigned long long> > d;
    d.assign(n*2+1, vector<unsigned long long> (n*2+1));

    d[0][0] = 1;
    for(int i = 1; i <= n*2; ++i){
        for(int j = 0; j <= n*2; ++j){
            if(i > 0 && j > 0){
                d[i][j] += d[i-1][j-1];
            }
            if(i > 0 && j < n*2){
                d[i][j] += d[i-1][j+1];
            }
        }
    }

    //cout << d[1][1] << " ";
    unsigned long long cnt = 0;
    unsigned long long ans = 0;
    n = n*2;
    for(int i = n - 1; i >= 0; --i){
         //cout << d[i][cnt+1] << " ";
             ++cnt;
         } else{
             ans += d[i][cnt-1];
             --cnt;
         }
    }

    cout << ans;
    return 0;
}
