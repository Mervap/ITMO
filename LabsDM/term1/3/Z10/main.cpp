#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int n, balance;
vector<int> a;

void print(int lvl){
    for(int i = 1; i < lvl-1; ++i){
        cout << a[i] << "+";
    }
    cout << a[lvl-1] << "\n";
}

void check(int lvl, int sum){
    if(sum == n){
        print(lvl);
        return;
    }

    for(int i = a[lvl-1]; i <= n; ++i){
        if(i + sum <= n){
            a[lvl] = i;
            check(lvl + 1, sum + i);
        }
    }
}


int main()
{
    freopen("partition.in", "r", stdin);
    freopen("partition.out", "w", stdout);


    cin >> n;
    a.assign(n+1, 0);

    a[0] = 1;
    check(1, 0);

    return 0;
}
