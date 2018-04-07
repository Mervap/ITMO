#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int n;
vector<int> a;
vector<bool> was;

void print() {
    for (int i = 0; i < n; ++i) {
        cout << a[i] << " ";
    }
    cout << "\n";
}

void check(int lvl) {
    if (lvl == n) {
        print();
        return;
    }

    for (int i = 0; i < n; ++i) {
        if (was[i] == false) {
            a[lvl] = i + 1;
            was[i] = true;
            check(lvl + 1);
            was[i] = false;
        }
    }
}


int main() {
    freopen("permutations.in", "r", stdin);
    freopen("permutations.out", "w", stdout);

    cin >> n;
    a.assign(n, 0);
    was.assign(n, false);

    check(0);

    return 0;
}
