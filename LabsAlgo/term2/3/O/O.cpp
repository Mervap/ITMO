#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

const int K = 18;

vector<int> par;
vector<int> in;
vector<int> out;
vector<int> cost;
vector<vector<int>> dp;
vector<vector<int>> max_rad;

struct vertex {
    int to, cost;
};

vector<vector<vertex>> g;

int n;

bool isLCA(int v, int u) {
    return in[v] <= in[u] && out[u] <= out[v];
}

int lca(int v, int u) {
    for (int k = K - 1; k >= 0; --k) {
        if (!isLCA(dp[v][k], u)) {
            v = dp[v][k];
        }
    }

    return isLCA(v, u) ? v : par[v];
}

int get(int v, int anc) {
    int ret = INT_MAX;
    for (int k = K - 1; k >= 0; k--) {
        if (!isLCA(dp[v][k], anc)) {
            ret = min(ret, max_rad[v][k]);
            v = dp[v][k];
        }
    }
    return v == anc ? ret : min(ret, cost[v]);
}

int tree = 0;

void dfs(int v, int p, int c) {
    par[v] = p;
    cost[v] = c;
    in[v] = tree++;
    for (vertex u : g[v]) {
        dfs(u.to, v, u.cost);
    }
    out[v] = tree++;
}

int main() {

    freopen("minonpath.in", "r", stdin);
    freopen("minonpath.out", "w", stdout);

    scanf("%d", &n);

    par.resize(n);
    in.resize(n);
    out.resize(n);
    cost.resize(n);
    g.resize(n);
    dp.assign(n, vector<int>(K));
    max_rad.assign(n, vector<int>(K));

    for (int i = 1; i < n; ++i) {
        int x, c;
        scanf("%d%d", &x, &c);
        g[x - 1].push_back({i, c});
    }

    dfs(0, 0, 0);

    for (int i = 0; i < n; ++i) {
        dp[i][0] = par[i];
        max_rad[i][0] = cost[i];
    }

    for (int k = 1; k < K; ++k) {
        for (int i = 0; i < n; ++i) {
            dp[i][k] = dp[dp[i][k - 1]][k - 1];
            max_rad[i][k] = min(max_rad[i][k - 1], max_rad[dp[i][k - 1]][k - 1]);
        }
    }

    int m;
    scanf("%d", &m);

    for (int i = 0; i < m; ++i) {
        int a, b;
        scanf("%d%d", &a, &b);
        --a, --b;

        int l = lca(a, b);
        printf("%d\n", min(get(a, l), get(b, l)));
    }
}