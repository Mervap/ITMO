#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

int n, m = 0;
vector<multiset<int>> edg;
vector<bool> used;
vector<int> ans;

void dfs(int v, int &cnt) {
    used[v] = true;

    for (auto e : edg[v]) {
        ++cnt;
        if (!used[e]) {
            dfs(e, cnt);
        }
    }
}

void euler(int v) {
    while (!edg[v].empty()) {
        int u = *edg[v].begin();
        edg[u].erase(edg[u].find(v));
        edg[v].erase(edg[v].begin());
        euler(u);
    }
    ans.push_back(v + 1);
}

int main() {
    ios_base::sync_with_stdio(false);
    cin >> n;
    edg.resize(n);
    for (int i = 0; i < n; ++i) {
        int mi, a;
        cin >> mi;
        m += mi;
        for (int j = 0; j < mi; ++j) {
            cin >> a;
            --a;
            edg[i].insert(a);
        }
    }

    int cnt = 0;
    for (int i = 0; i < n; ++i) {
        if (edg[i].size() % 2 == 1) {
            ++cnt;
        }
    }

    if (cnt != 2 && cnt != 0) {
        cout << -1;
        return 0;
    }

    used.resize(n);
    cnt = 0;
    dfs(0, cnt);
    if (cnt != m) {
        cout << -1;
        return 0;
    }

    euler(0);

    cout << ans.size() - 1 << "\n";
    for (auto i : ans) {
        cout << i << " ";
    }

    return 0;
}