#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

int d, k;
vector<multiset<int>> edg;
vector<int> ans;

void euler(int v) {
    while (!edg[v].empty()) {
        int u = *edg[v].begin();
        edg[v].erase(edg[v].begin());
        euler(u);
    }
    ans.push_back(v);
}

int main() {
    ios_base::sync_with_stdio(false);
    cin >> d >> k;

    if (k == 1) {
        for (int i = 0; i < d; ++i) {
            cout << i;
        }
        return 0;
    }

    int n = 1;
    for (int i = 1; i < k; ++i) {
        n *= d;
    }

    edg.resize(n);
    for (int i = 0; i < n; ++i) {
        int m = (i % (n / d)) * d;
        for (int j = 0; j < d; ++j) {
            edg[i].insert(m + j);
        }
    }

    euler(0);

    for (int i = 0; i < k - 1; ++i) {
        cout << 0;
    }

    reverse(ans.begin(), ans.end());
    for (int i = 1; i < ans.size(); ++i) {
        cout << ans[i] % d;
    }

    return 0;
}