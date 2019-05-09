//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

struct tt {
    int e, n;
};

vector<bool> used;
vector<vector<tt>> e;
vector<int> up;
vector<int> num;
vector<int> bridges;

int l = 0;

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
        bridges.push_back(p);
    }
}

int main() {
    int n, m;
    freopen("bridges.in", "r", stdin);
    freopen("bridges.out", "w", stdout);
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;
    e.resize(n);
    used.resize(n);
    num.assign(n, 1000000);
    up.resize(n, 1000000);

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

    cout << bridges.size() << "\n";
    sort(bridges.begin(), bridges.end());
    for (auto b : bridges) {
        cout << b + 1 << " ";
    }
    return 0;
}