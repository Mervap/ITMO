#include <iostream>
#include <vector>
#include <set>
#include <queue>

using namespace std;

int n, m, max_c;

struct DSU {

    explicit DSU(int n) : p(vector<int>(n)), r(vector<int>(n)) {
        for (int i = 0; i < n; ++i) {
            p[i] = i;
            r[i] = 0;
        }
    }

    int get(int v) {
        if (p[v] == v) {
            return v;
        } else {
            return p[v] = get(p[v]);
        }
    }

    void un(int v, int u) {
        int pv = get(v);
        int pu = get(u);

        if (r[pv] < r[pu]) {
            p[pv] = pu;
        } else {
            p[pu] = pv;
            if (r[pv] == r[pu]) {
                ++r[pv];
            }
        }
    }

private:
    vector<int> p;
    vector<int> r;
};

struct edge {
    int b;
    int e;
    int color;
};

vector<edge> edg;
vector<bool> inA;

int findPath(vector<vector<int>> &g, vector<int> &S, vector<int> &T, vector<int> &from) {
    vector<int> d(m, 1000000);
    queue<int> q;
    for (auto i : S) {
        q.push(i);
        d[i] = 0;
        from[i] = -1;
    }

    while (!q.empty()) {
        int cur = q.front();
        q.pop();

        for (auto e : g[cur]) {
            if (d[cur] + 1 < d[e]) {
                d[e] = d[cur] + 1;
                from[e] = cur;
                q.push(e);
            }
        }
    }

    int min_d = 1000000;
    int min_v = -1;
    for (auto i : T) {
        if (d[i] < min_d) {
            min_d = d[i];
            min_v = i;
        }
    }

    return min_v;
}

bool update() {
    vector<vector<int>> d(m);

    for (int i = 0; i < m; ++i) {
        if (!inA[i]) {
            continue;
        }

        DSU dsu(n);
        vector<bool> usedColor(max_c);
        for (int j = 0; j < m; ++j) {
            if (i != j && inA[j]) {
                dsu.un(edg[j].b, edg[j].e);
                usedColor[edg[j].color] = true;
            }
        }

        for (int j = 0; j < m; ++j) {
            if (!inA[j]) {
                if (dsu.get(edg[j].b) != dsu.get((edg[j].e))) {
                    d[i].push_back(j);
                }

                if (!usedColor[edg[j].color]) {
                    d[j].push_back(i);
                }
            }
        }
    }

    vector<int> S, T;
    DSU dsu(n);
    vector<bool> usedColor(max_c);
    for (int i = 0; i < m; ++i) {
        if (inA[i]) {
            dsu.un(edg[i].b, edg[i].e);
            usedColor[edg[i].color] = true;
        }
    }

    for (int i = 0; i < m; ++i) {
        if (!inA[i]) {
            if (dsu.get(edg[i].b) != dsu.get((edg[i].e))) {
                S.push_back(i);
            }

            if (!usedColor[edg[i].color]) {
                T.push_back(i);
            }
        }
    }

    vector<int> from(m, -1);

    int t = findPath(d, S, T, from);
    if (t == -1) {
        return false;
    } else {
        while (t != -1) {
            inA[t] = !inA[t];
            t = from[t];
        }
        return true;
    }
}


int main() {

    freopen("rainbow.in", "r", stdin);
    freopen("rainbow.out", "w", stdout);

    cin >> n >> m;

    int a, b, c;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        edg.push_back({a - 1, b - 1, c - 1});
        max_c = std::max(max_c, c);
    }

    inA.resize(m);
    while (update()) {};

    vector<int> ans;
    for (int i = 0; i < m; ++i) {
        if (inA[i]) {
            ans.push_back(i + 1);
        }
    }

    cout << ans.size() << "\n";
    for (auto i : ans) {
        cout << i << " ";
    }
    return 0;
}