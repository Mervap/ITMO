#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int n, k;
vector<int> a;
vector<bool> was;

void print() {
    for (int i = 1; i <= k; ++i) {
        cout << a[i] << " ";
    }
    cout << "\n";
}

void check(int lvl) {
    if (lvl == k + 1) {
        print();
        return;
    }

    for (int i = a[lvl - 1] + 1; i <= n; ++i) {
        if (was[i] == false) {
            a[lvl] = i;
            was[i] = true;
            check(lvl + 1);
            was[i] = false;
        }
    }
}


int main() {
    freopen("choose.in", "r", stdin);
    freopen("choose.out", "w", stdout);

    cin >> n >> k;
    a.assign(k + 1, 0);
    was.assign(n + 1, false);

    a[0] = 0;
    check(1);

    return 0;
}
