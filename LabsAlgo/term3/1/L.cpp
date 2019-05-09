//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <map>

using namespace std;

struct tt {
    int to, n;
};


vector<bool> used;
vector<vector<tt>> e;
vector<int> up;
vector<int> num;
vector<int> color;

int l = 0;
int max_color = 0;

void dfs(int v, int p) {
    used[v] = true;
    num[v] = l++;
    up[v] = num[v];
    int cnt = 0;

    for (auto e : e[v]) {
        if (e.to == p) {
            continue;
        }

        if (!used[e.to]) {
            dfs(e.to, v);
            up[v] = min(up[v], up[e.to]);
            ++cnt;
        } else {
            up[v] = min(up[v], num[e.to]);
        }
    }
}

void dfs2(int v, int p, int c) {
    used[v] = true;

    for (auto e : e[v]) {
        if (e.to == p) {
            continue;
        }

        if (!used[e.to]) {
            if (up[e.to] >= num[v]) {
                color[e.n] = ++max_color;
                dfs2(e.to, v, max_color);
            } else {
                color[e.n] = c;
                dfs2(e.to, v, c);
            }
        } else if (!color[e.n]) {
            color[e.n] = c;
        }
    }
}

int main() {
    int n, m;
    freopen("biconv.in", "r", stdin);
    freopen("biconv.out", "w", stdout);
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;
    e.resize(n);
    used.resize(n);
    num.assign(n, 1000000);
    up.resize(n, 1000000);
    color.resize(m);

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

    used.assign(n, false);
    for (int i = 0; i < n; ++i) {
        if (!used[i]) {
            dfs2(i, -1, ++max_color);
        }
    }

    map<int, int> mp;
    int cnt = 0;
    for (auto i : color) {
        if (mp[i] == 0) {
            mp[i] = ++cnt;
        }
    }

    cout << cnt << "\n";
    for (auto i : color) {
        cout << mp[i] << " ";
    }
    return 0;
}