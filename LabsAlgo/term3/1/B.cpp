//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector<bool> used;
vector<vector<int>> e;
vector<vector<int>> b_edg;
vector<pair<int, int>> tout;
vector<int> color;
int t = 0;
int c = 0;

void dfs1(int v) {
    used[v] = true;

    for (auto e : e[v]) {
        if (!used[e]) {
            dfs1(e);
        }
    }

    tout.push_back({t++, v});
}

void dfs2(int v) {
    color[v] = c;

    for (auto e : b_edg[v]) {
        if (!color[e]) {
            dfs2(e);
        }
    }
}

int main() {
    int n, m;
    freopen("cond.in", "r", stdin);
    freopen("cond.out", "w", stdout);
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;
    e.resize(n);
    b_edg.resize(n);
    used.resize(n);
    color.resize(n);

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        e[a].push_back(b);
        b_edg[b].push_back(a);
    }

    for (int i = 0; i < n; ++i) {
        if (!used[i]) {
            dfs1(i);
        }
    }

    sort(tout.begin(), tout.end());

    for (int i = tout.size() - 1; i >= 0; --i) {
        if (!color[tout[i].second]) {
            ++c;
            dfs2(tout[i].second);
        }
    }

    cout << c << "\n";
    for (auto l: color) {
        cout << l << " ";
    }

    return 0;
}