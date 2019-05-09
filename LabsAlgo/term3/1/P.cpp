//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <set>

using namespace std;
int n, m;
vector<set<int>> edg;
vector<set<int>> b_edg;
vector<bool> used;
vector<pair<int, int>> tout;
vector<int> color;
int t = 0;
int cc = 0;

void add_e(int a, int b) {
    edg[a + n].insert(b + n);
    b_edg[b + n].insert(a + n);
}


void dfs1(int v) {
    used[v] = true;

    for (auto e : edg[v]) {
        if (!used[e]) {
            dfs1(e);
        }
    }

    tout.push_back({t++, v});
}

void dfs2(int v) {
    color[v] = cc;

    for (auto e : b_edg[v]) {
        if (!color[e]) {
            dfs2(e);
        }
    }
}

int main() {
    cin >> n >> m;
    edg.resize(2 * n + 1);
    b_edg.resize(2 * n + 1);
    used.resize(2 * n + 1);
    color.resize(2 * n + 1);

    int a, b, c;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        // <a, b, c> = (a || b) && (b || c) && (a || c)
        add_e(-a, b);
        add_e(-b, a);
        add_e(-b, c);
        add_e(-c, b);
        add_e(-a, c);
        add_e(-c, a);
    }

    for (int i = 0; i < 2 * n + 1; ++i) {
        if (i != n && !used[i]) {
            dfs1(i);
        }
    }

    sort(tout.begin(), tout.end());

    cc = 0;
    for (int i = tout.size() - 1; i >= 0; --i) {
        if (!color[tout[i].second]) {
            ++cc;
            dfs2(tout[i].second);
        }
    }

    for (int i = 1; i <= n; ++i) {
        if (color[i + n] == color[-i + n]) {
            cout << "NO";
            return 0;
        }
    }

    cout << "YES\n";
    for (int i = 1; i <= n; ++i) {
        cout << (color[i + n] > color[-i + n] ? i : -i) << " ";
    }
    return 0;
}