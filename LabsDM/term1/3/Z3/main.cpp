#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int n;
vector<int> a;
vector< vector<int> > b;

void check(int lvl){
    if(lvl == n){
        b.push_back(a);
        return;
    }

    a[lvl] = 0;
    check(lvl + 1);
    a[lvl] = 1;
    check(lvl + 1);
    a[lvl] = 2;
    check(lvl + 1);
}


int main()
{
    freopen("antigray.in", "r", stdin);
    freopen("antigray.out", "w", stdout);


    cin >> n;
    a.assign(n, 0);
    check(1);

    for(int i = 0; i < (int) b.size(); ++i){
        for(int k = 0; k < 3; ++k){
            for(int j = 0; j < n; ++j){
                cout << (b[i][j]+k)%3;
            }
            cout << "\n";
        }
    }

    return 0;
}
