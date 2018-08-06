#include <iostream>
#include <vector>
#include <algorithm>
#include <set>

using namespace std;

const int MX = 100000;

vector<int> par(MX);
vector<int> r(MX);
vector<int> anc(MX);
vector<int> balance(MX);

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
vector<vector<int>> q(MX);
vector<bool> was(MX);
int ans = 0;

int n = 0, m = 0;

set<pair<int, int>> was2;

int dfs(int v) {
    was[v] = true;
    par[v] = v;
    anc[v] = v;

    int b = 0;
    for (int u : edg[v]) {
        if (!was[u]) {
            b += dfs(u);
            un(v, u);
        }
    }

    for (auto u : q[v]) {
        if (was[u] && !was2.count({u, v})) {
            balance[anc[get(u)]] -= 2;
            was2.insert({v, u});
        }
    }

    if (b + balance[v] == 0) {
        ++ans;
    }

    return b + balance[v];
}

int main() {

    //freopen("N.in", "r", stdin);
    //freopen("N.out", "w", stdout);

    scanf("%d\n", &n);

    int a, b;
    for (size_t i = 0; i < n - 1; ++i) {
        scanf("%d %d", &a, &b);
        --a, --b;
        edg[a].push_back(b);
        edg[b].push_back(a);
    }

    scanf("%d\n", &m);
    for (size_t i = 0; i < m; ++i) {
        scanf("%d %d", &a, &b);
        --a, --b;
        q[a].push_back(b);
        q[b].push_back(a);
        ++balance[a];
        ++balance[b];
    }

    dfs(0);

    printf("%d\n", ans - 1);
}