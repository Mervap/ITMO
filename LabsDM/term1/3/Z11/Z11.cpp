#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int n;
vector<int> a;
vector<bool> b;
vector<vector<int> > ans;

void print() {
    vector<int> c;
    for (int i = 0; i < n; ++i) {
        if (b[i]) {
            c.push_back(a[i]);
        }
    }
    ans.push_back(c);
}

void check(int lvl) {
    if (lvl == n) {
        print();
        return;
    }

    b[lvl] = false;
    check(lvl + 1);
    b[lvl] = true;
    check(lvl + 1);
}


int main() {
    freopen("subsets.in", "r", stdin);
    freopen("subsets.out", "w", stdout);


    cin >> n;
    for (int i = 0; i < n; i++) {
        a.push_back(i + 1);
        b.push_back(false);
    }

    check(0);
    sort(ans.begin(), ans.end());

    for (int i = 0; i < (int) ans.size(); ++i) {
        for (int j = 0; j < (int) ans[i].size(); ++j) {
            cout << ans[i][j] << " ";
        }
        cout << "\n";
    }
    return 0;
}
