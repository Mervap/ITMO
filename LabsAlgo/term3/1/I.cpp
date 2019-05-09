//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

vector<bool> used;
vector<vector<int>> e;
vector<int> up;
vector<int> num;
set<int> dots;

int l = 0;

void dfs(int v, int p) {
    used[v] = true;
    num[v] = l++;
    up[v] = num[v];
    int cnt = 0;

    for (auto e : e[v]) {
        if (e == p) {
            continue;
        }

        if (!used[e]) {
            dfs(e, v);
            up[v] = min(up[v], up[e]);
            if (up[e] >= num[v] && p != -1) {
                dots.insert(v);
            }
            ++cnt;
        } else {
            up[v] = min(up[v], num[e]);
        }
    }

    if (p == -1 && cnt > 1) {
        dots.insert(v);
    }
}

int main() {
    int n, m;
    freopen("points.in", "r", stdin);
    freopen("points.out", "w", stdout);
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
        e[a].push_back(b);
        e[b].push_back(a);
    }

    for (int i = 0; i < n; ++i) {
        if (!used[i]) {
            dfs(i, -1);
        }
    }

    cout << dots.size() << "\n";
    for (auto b : dots) {
        cout << b + 1 << "\n";
    }
    return 0;
}