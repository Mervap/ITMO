//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

vector<int> color;
vector<vector<int>> e;

void dfs(int v, int p, int c) {
    color[v] = c;

    for (auto e : e[v]) {
        if (e == p) {
            continue;
        }

        if (color[e] == -1) {
            dfs(e, v, 1 - c);
        } else if (color[e] == color[v]) {
            cout << "NO";
            exit(0);
        }
    }
}

int main() {
    int n, m;
    freopen("bipartite.in", "r", stdin);
    freopen("bipartite.out", "w", stdout);
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;
    e.resize(n);
    color.assign(n, -1);

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        e[a].push_back(b);
        e[b].push_back(a);
    }

    for (int i = 0; i < n; ++i) {
        if (color[i] == -1) {
            dfs(i, -1, 0);
        }
    }

    cout << "YES";
    return 0;
}