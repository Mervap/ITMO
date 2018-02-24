
#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{
   // freopen("brackets2num2.in", "r", stdin);
    //freopen("brackets2num2.out", "w", stdout);


    unsigned long long n;
    string s;
    cin >> s;
    n = s.length()/2;

    vector< vector<unsigned long long> > d;
    d.assign(n*2+1, vector<unsigned long long> (n*2+1));

    d[0][0] = 1;
    for(int i = 0; i <= n*2; ++i){
        for(int j = 0; j <= n*2; ++j){
            if(i > 0 && j > 0){
                d[i][j] += d[i-1][j-1];
            }
            if(i > 0 && j < n*2){
                d[i][j] += d[i-1][j+1];
            }
        }
    }

    for(int i = 0; i <= n*2; ++i){
        for(int j = 0; j <= n*2; ++j){
            cout << d[i][j] << " ";
        }
        cout << "\n";
    }

    int cnt = 0;
    vector<char> last(n*2);
    int l = 0;
    unsigned long long ans = 0;
    for (int i=n*2-1; i>=0; --i) {
        //cout << d[i][cnt+1] << " ";
        if (s[n*2 - 1 - i] == '('){
            last[l] = '(';
            ++l;
            ++cnt;
        } else{
            ans += d[i][cnt+1]*(1ull << (i-cnt-1)/2);
            if(l > 0 && last[l-1] == '(' && s[n*2 - 1 - i] == ')'){
                --l;
                --cnt;
            } else{
                if(l > 0 && last[l-1] == '('){
                    ans += d[i][cnt-1]*(1ull << (i-cnt+1)/2);
                }
                if(s[n*2 - 1 - i] == '['){
                    last[l] = '[';
                    ++l;
                    ++cnt;
                } else{
                    ans += d[i][cnt+1]*(1ull << (i-cnt-1)/2);
                    --l;
                    --cnt;
                }
            }
        }
    }
    cout << ans;
    return 0;
}
