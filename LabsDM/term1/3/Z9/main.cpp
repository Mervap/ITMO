#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int n, balance;
vector<char> a;

void print(){
    for(int i = 0; i < 2*n; ++i){
        cout << a[i];
    }
    cout << "\n";
}

void check(int lvl){
    if(lvl == 2*n){
        print();
        return;
    }

    if(2*n - lvl > balance){
        a[lvl] = '(';
        ++balance;
        check(lvl + 1);
        --balance;
    }

    if(balance > 0){
        a[lvl] = ')';
        --balance;
        check(lvl + 1);
        ++balance;
    }
}


int main()
{
    freopen("brackets.in", "r", stdin);
    freopen("brackets.out", "w", stdout);


    cin >> n;
    a.assign(2*n, '(');

    check(0);

    return 0;
}
