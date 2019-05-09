//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector<bool> used;
vector<vector<int>> e;
vector<int> color;
int t = 0;
int c = 0;

void dfs(int v) {
    color[v] = c;

    for (auto e : e[v]) {
        if (!color[e]) {
            dfs(e);
        }
    }
}

int main() {
    int n, m;
    freopen("components.in", "r", stdin);
    freopen("components.out", "w", stdout);
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;
    e.resize(n);
    used.resize(n);
    color.resize(n);

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        e[a].push_back(b);
        e[b].push_back(a);
    }

    for (int i = 0; i < n; ++i) {
        if (!color[i]) {
            ++c;
            dfs(i);
        }
    }

    cout << c << "\n";
    for (auto l: color) {
        cout << l << " ";
    }

    return 0;
}