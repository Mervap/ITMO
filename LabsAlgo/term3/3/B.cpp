#include <iostream>
#include <vector>
#include <set>

using namespace std;

int n, m, k;
vector<vector<int>> edg;
vector<bool> used;
vector<int> p;

bool dfs(int v) {
    if (used[v]) {
        return false;
    }
    used[v] = true;
    for (auto e : edg[v]) {
        if (p[e] == -1 || dfs(p[e])) {
            p[e] = v;
            return true;
        }
    }
    return false;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin >> n >> m;
    edg.resize(n);
    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        edg[a].push_back(b);
    }

    p.assign(n, -1);
    for (int i = 0; i < n; ++i) {
        used.assign(n, false);
        dfs(i);
    }

    int ans = 0;
    for (auto i : p) {
        ans += (i != -1);
    }

    cout << n - ans;
}