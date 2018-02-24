#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int n;
vector<int> a;

void print(){
    for(int i = 0; i < n; ++i){
        cout << a[i];
    }
    cout << "\n";
}

void check(int lvl){
    if(lvl == n){
        print();
        return;
    }

    a[lvl] = 0;
    check(lvl + 1);
    a[lvl] = 1;
    check(lvl + 1);
}


int main()
{
    freopen("allvectors.in", "r", stdin);
    freopen("allvectors.out", "w", stdout);


    cin >> n;
    a.assign(n, 0);

    check(0);

    return 0;
}
