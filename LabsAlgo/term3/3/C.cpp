#include <iostream>
#include <vector>
#include <set>

using namespace std;

int n, m;
vector<vector<int>> edg;
vector<bool> used;
set<int> lm;
set<int> rp;

void dfs(int v) {
    used[v] = true;
    if (v < m) {
        lm.erase(v);
    } else {
        rp.insert(v);
    }

    for (auto e : edg[v]) {
        if (!used[e]) {
            dfs(e);
        }
    }
}

int main() {

    ios_base::sync_with_stdio(false);
    cin >> m >> n;

    edg.resize(m + n);
    int k;
    for (int i = 0; i < m; ++i) {
        cin >> k;
        int b;
        for (int j = 0; j < k; ++j) {
            cin >> b;
            --b;
            edg[i].push_back(m + b);
        }
    }

    set<int> bad;
    int a;
    for (int i = 0; i < m; ++i) {
        cin >> a;
        --a;
        if (a == -1) {
            bad.insert(i);
        } else {
            edg[m + a].push_back(i);
        }
    }

    for (int i = 0; i < m; ++i) {
        lm.insert(i);
    }

    used.resize(m + n);
    for (auto v : bad) {
        if (!used[v]) {
            dfs(v);
        }
    }

    cout << lm.size() + rp.size() << "\n";
    cout << lm.size();
    for (auto l : lm) {
        cout << " " << l + 1;
    }

    cout << "\n" << rp.size();
    for (auto r : rp) {
        cout << " " << r + 1 - m;
    }


    return 0;
}