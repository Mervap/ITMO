//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <map>

using namespace std;

struct tt {
    int e, n;
};


vector<bool> used;
vector<vector<tt>> e;
vector<int> up;
vector<int> num;
vector<bool> bridges;
vector<int> colors;

int l = 0;
int max_color = 0;

void dfs(int v, int p) {
    used[v] = true;
    num[v] = l++;
    up[v] = num[v];

    for (auto e : e[v]) {
        if (!used[e.e]) {
            dfs(e.e, e.n);
        }

        if (e.n != p) {
            up[v] = min(up[v], up[e.e]);
        }
    }

    if (up[v] == num[v] && p != -1) {
        bridges[p] = true;
    }
}

void dfs2(int v, int c) {
    colors[v] = c;

    for (auto e : e[v]) {
        if (!colors[e.e]) {
            if (bridges[e.n]) {
                dfs2(e.e, ++max_color);
            } else {
                dfs2(e.e, c);
            }
        }
    }

}

int main() {
    int n, m;
    freopen("bicone.in", "r", stdin);
    freopen("bicone.out", "w", stdout);
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;
    e.resize(n);
    used.resize(n);
    num.assign(n, 1000000);
    up.resize(n, 1000000);
    bridges.resize(m);

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        e[a].push_back({b, i});
        e[b].push_back({a, i});
    }

    for (int i = 0; i < n; ++i) {
        if (!used[i]) {
            dfs(i, -1);
        }
    }

    colors.resize(n);
    for (int i = 0; i < n; ++i) {
        if (!colors[i]) {
            dfs2(i, ++max_color);
        }
    }

    map<int, int> mp;
    int cnt = 0;
    cout << max_color << "\n";
    for (auto i : colors) {
        if (mp[i] == 0) {
            mp[i] = ++cnt;
        }
        cout << mp[i] << " ";
    }

    return 0;
}