#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

#define next nex
#define f first
#define s second
#define int long long

using namespace std;

vector<int> head, next, edg, kol, d, kol_v, d_v, w, ans;
int n, m;

pair<int, int> dfs_1(int i, int p) {
    int h = head[i];
    while (h != -1) {
        if (edg[h] != p) {
            pair<int, int> v = dfs_1(edg[h], i);
            d[i] += v.f + kol[edg[h]];
            kol[i] += v.s;
        }
        h = next[h];
    }

    return make_pair(d[i], kol[i]);
}

void dfs_2(int i, int p) {
    int h = head[i];
    int v;
    while (h != -1) {
        if (edg[h] != p) {
            v = edg[h];
            kol_v[v] = n - kol[v];
            d_v[v] = d_v[i] + d[i] - (d[v] + kol[v]) + kol_v[v];
            ans[w[h]] = kol[v] * d_v[v] + d[v] * kol_v[v];
            dfs_2(v, i);
        }
        h = next[h];
    }
}

int_fast32_t main() {
    freopen("treedp.in", "r", stdin);
    freopen("treedp.out", "w", stdout);

    n = 0;
    cin >> n;

    head.assign(n + 1, -1);
    kol.assign(n + 1, 1);
    d.assign(n + 1, 0);
    ans.assign(n, 0);
    w.assign(2 * n, 0);
    kol_v.assign(n + 1, 0);
    d_v.assign(n + 1, 0);
    next.assign(2 * (n + 1), -1);
    edg.assign(2 * (n + 1), -1);

    int a, b, p = -1;
    for (int i = 0; i < n - 1; ++i) {
        cin >> a >> b;
        ++p;
        next[p] = head[a];
        head[a] = p;
        edg[p] = b;
        w[p] = i;

        ++p;
        next[p] = head[b];
        head[b] = p;
        edg[p] = a;
        w[p] = i;
    }

    dfs_1(1, -1);
    dfs_2(1, -1);

    for (int i = 0; i < n - 1; ++i) {
        cout << ans[i] << "\n";
    }

    return 0;
}
