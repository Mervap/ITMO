#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

const int MX = 500000;

struct vertex {
    int to, n;
};

vector<int> par(MX);
vector<int> r(MX);
vector<int> anc(MX);

int get(int v) {
    if (par[v] == v) {
        return v;
    }

    return par[v] = get(par[v]);
}

void un(int v, int u) {
    int x = get(v);
    int y = get(u);

    if (r[x] == r[y]) {
        ++r[x];
    }

    if (r[y] < r[x]) {
        par[x] = y;
        anc[y] = v;
    } else {
        par[y] = x;
        anc[x] = v;
    }
}

vector<vector<int>> edg(MX);
vector<vector<vertex>> q(MX);
vector<bool> was(MX);
vector<int> ans(MX);

int n = 0, m = 0;

void dfs(int v) {
    was[v] = true;
    par[v] = v;
    anc[v] = v;
    for (int u : edg[v]) {
        if (!was[u]) {
            dfs(u);
            un(v, u);
        }
    }

    for (auto u : q[v]) {
        if (was[u.to]) {
            ans[u.n] = anc[get(u.to)] + 1;
        }
    }
}

int main() {

    freopen("lca.in", "r", stdin);
    freopen("lca.out", "w", stdout);

    int t;
    scanf("%d\n", &t);

    string s;
    int a, b;
    for (size_t i = 0; i < t; ++i) {
        cin >> s;
        scanf("%d %d", &a, &b);
        --a, --b;
        if (s[0] == 'A') {
            ++n;
            edg[a].push_back(b);
            edg[b].push_back(a);
        } else {
            q[a].push_back({b, m});
            q[b].push_back({a, m});
            ++m;
        }
    }

    dfs(0);

    for (int i = 0; i < m; ++i) {
        printf("%d\n", ans[i]);
    }
}