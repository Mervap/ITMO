#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{

    freopen("levenshtein.in", "r", stdin);
    freopen("levenshtein.out", "w", stdout);

    string s, s1;

    cin >> s >> s1;

    int l = (int) s.length();
    int l1 = (int) s1.length();

    vector <vector <int> > ans;
    ans.assign(max(l,l1)+1, vector<int> (max(l, l1)+1));

    for(int i = 1; i <= l; ++i){
        ans[i][0] = i;
    }

    for(int j = 1; j <= l1; ++j){
        ans[0][j] = j;
    }

    int k;
    for(int i = 1; i <= l; ++i){
        for(int j = 1; j <= l1; ++j){
            if(s[i-1] == s1[j-1]){
                k = 0;
            } else{
                k = 1;
            }

            ans[i][j] = min(min(ans[i-1][j] + 1, ans[i][j-1] + 1), ans[i-1][j-1] + k);
        }
    }

    cout << ans[l][l1];
    return 0;
}
