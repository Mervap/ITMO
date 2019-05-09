#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

int n;
vector<vector<int>> edg;
vector<bool> used;
vector<int> p, ans;

bool dfs(int v) {
    if (used[v]) {
        return false;
    }
    used[v] = true;
    for (auto e : edg[v]) {
        if (p[e] == -1 || dfs(p[e])) {
            p[e] = v;
            ans[v] = e;
            return true;
        }
    }
    return false;
}

int main() {
    ios_base::sync_with_stdio(false);

    freopen("matching.in", "r", stdin);
    freopen("matching.out", "w", stdout);

    cin >> n;
    vector<pair<int, int>> v;
    int a;
    for (int i = 0; i < n; ++i) {
        cin >> a;
        v.push_back({a, i});
    }

    edg.resize(n);
    for (int i = 0; i < n; ++i) {
        int mi;
        cin >> mi;
        for(int j = 0; j < mi; ++j) {
            cin >> a;
            --a;
            edg[i].push_back(a);
        }
    }

    sort(v.begin(), v.end());
    p.assign(n, -1);
    ans.assign(n, - 1);
    for (int i = n - 1; i >= 0; --i) {
        used.assign(n, false);
        dfs(v[i].second);
    }

    for (auto i : ans) {
        cout << i + 1 << " ";
    }
}