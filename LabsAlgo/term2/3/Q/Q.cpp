#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

const int MX = 100001;

int n, m;
vector<int> nx(MX, -1);
vector<int> size(MX);
vector<int> par(MX);
vector<int> chain(MX);
vector<int> chain_cnt(MX);
vector<int> top(MX);
vector<int> depth(MX);
vector<int> in(MX);
vector<vector<int>> g(MX);

vector<int> tree(4 * MX);

void update(int i, int l, int r, int pos, int val) {
    if (l == r) {
        tree[i] += val;
        return;
    }

    int m = (l + r) / 2;
    if (pos <= m) {
        update(2 * i, l, m, pos, val);
    } else {
        update(2 * i + 1, m + 1, r, pos, val);
    }
    tree[i] = max(tree[2 * i], tree[2 * i + 1]);
}

int query(int i, int l, int r, int lz, int rz) {
    if (lz > rz) {
        return 0;
    }

    if (l == lz && r == rz) {
        return tree[i];
    }

    int m = (l + r) / 2;
    return max(query(2 * i, l, m, lz, min(m, rz)),
               query(2 * i + 1, m + 1, r, max(m + 1, lz), rz));
}

int find(int a, int b) {
    int res = 0;
    while (chain[a] != chain[b]) {
        if (depth[top[chain[a]]] < depth[top[chain[b]]]) {
            swap(a, b);
        }

        int start = top[chain[a]];
        res = max(res, query(1, 0, n - 1, in[start], in[a]));
        a = par[start];
    }

    if (depth[a] > depth[b]) {
        swap(a, b);
    }
    res = max(res, query(1, 0, n - 1, in[a], in[b]));
    return res;
}

int dfs(int v, int p) {
    par[v] = p;
    size[v] = 1;
    for (int u : g[v]) {
        if (u != p) {
            depth[u] = depth[v] + 1;
            size[v] += dfs(u, v);

            if (nx[v] == -1 || size[u] > size[nx[v]]) {
                nx[v] = u;
            }
        }
    }

    return size[v];
}

int t = 0, cnt = 0;

void hld(int v, int p) {
    chain[v] = cnt;
    in[v] = t++;
    if (chain_cnt[cnt] == 0) {
        top[cnt] = v;
    }

    ++chain_cnt[cnt];
    if (nx[v] != -1) {
        hld(nx[v], v);
    }

    for (int u : g[v]) {
        if (u == p || u == nx[v]) {
            continue;
        }
        ++cnt;
        hld(u, v);
    }
}

int main() {

    freopen("caves.in", "r", stdin);
    freopen("caves.out", "w", stdout);

    scanf("%d", &n);
    int a, b;
    for (int i = 0; i < n - 1; ++i) {
        scanf("%d %d", &a, &b);
        --a, --b;
        g[a].push_back(b);
        g[b].push_back(a);
    }
    dfs(0, 0);
    hld(0, -1);

    scanf("%d\n", &m);
    for (int i = 0; i < m; ++i) {
        char c;
        scanf("%c", &c);
        if (c == 'I') {
            scanf("%d %d\n", &a, &b);
            --a;
            update(1, 0, n - 1, in[a], b);
        } else {
            scanf("%d %d\n", &a, &b);
            --a, --b;
            printf("%d\n", find(a, b));
        }
    }
    return 0;
}