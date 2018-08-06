#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

const int K = 19;

vector<long long> tree;
vector<int> par;
vector<int> in;
vector<int> out;
vector<vector<int>> dp;

vector<vector<int>> g;

int n;

void update(int i, int l, int r, int pos, long long val) {
    if (l == r) {
        tree[i] += val;
        return;
    }

    int m = (r + l) / 2;
    if (pos <= m) {
        update(2 * i, l, m, pos, val);
    } else {
        update(2 * i + 1, m + 1, r, pos, val);
    }
    tree[i] = tree[2 * i] + tree[2 * i + 1];
}

long long query(int i, int l, int r, int lz, int rz) {
    if (lz > rz) {
        return 0;
    }

    if (l == lz && r == rz) {
        return tree[i];
    }

    int m = (l + r) / 2;
    return query(2 * i, l, m, lz, min(m, rz))
           + query(2 * i + 1, m + 1, r, max(m + 1, lz), rz);

}

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

int tree = 0;

void dfs(int v, int p) {
    par[v] = p;
    in[v] = tree++;
    for (int u : g[v]) {
        if (u != p) {
            dfs(u, v);
        }
    }
    out[v] = tree++;
}

int main() {

    freopen("treepathadd.in", "r", stdin);
    freopen("treepathadd.out", "w", stdout);

    scanf("%d", &n);

    ++n;
    tree.resize(8 * n);
    par.resize(n);
    in.resize(n);
    out.resize(n);
    g.resize(n);
    dp.assign(n, vector<int>(K));

    int a, b;
    for (int i = 1; i < n - 1; ++i) {
        scanf("%d %d", &a, &b);

        g[a].push_back(b);
        g[b].push_back(a);
    }

    g[0].push_back(1);
    dfs(0, 0);

    for (int i = 0; i < n; ++i) {
        dp[i][0] = par[i];
    }

    for (int k = 1; k < K; ++k) {
        for (int i = 0; i < n; ++i) {
            dp[i][k] = dp[dp[i][k - 1]][k - 1];
        }
    }

    int m;
    scanf("%d\n", &m);

    char c;
    long long val;

    int nn = 2 * n - 1;
    for (int i = 0; i < m; ++i) {
        scanf("%c", &c);

        if (c == '+') {
            scanf("%d %d %lld\n", &a, &b, &val);
            int l = lca(a, b);
            update(1, 0, nn, in[a], val);
            update(1, 0, nn, in[b], val);
            update(1, 0, nn, in[l], -val);
            update(1, 0, nn, in[par[l]], -val);
        } else {
            scanf("%d\n", &a);
            printf("%lld\n", query(1, 0, nn, in[a], out[a]));
        }

    }
}