#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int n;
vector<int> a;
vector< vector<int> > ans;

void check(int lvl){
    if(lvl == n){
        ans.push_back(a);
        return;
    }

    a[lvl] = 0;
    check(lvl + 1);
    if(lvl > 0 && a[lvl-1] == 1){
        return;
    }
    a[lvl] = 1;
    check(lvl + 1);
}


int main()
{
    freopen("vectors.in", "r", stdin);
    freopen("vectors.out", "w", stdout);


    cin >> n;
    a.assign(n, 0);

    check(0);

    cout << (int)ans.size() << "\n";
    for(int i = 0; i < (int)ans.size(); ++i){
        for(int j = 0; j < n; ++j){
            cout << ans[i][j];
        }
        cout << "\n";
    }

    return 0;
}
