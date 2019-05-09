//O(E*log^2E)

#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <map>
#include <cassert>
#include <algorithm>

using namespace std;
const int WHITE = 0;
const int GRAY = 1;
const int BLACK = 2;


vector<long long> d;

struct edge {
    int from;
    long long w;

    bool operator<(edge const &other) const {
        return w < other.w || (w == other.w && from < other.from);
    }
};

int n, m, s = 0;
vector<set<edge>> edg;
vector<int> dsu;
vector<int> r;

int get(int v) {
    if (dsu[v] == v) {
        return v;
    } else {
        return get(dsu[v]);
    }
}

void un(int v, int u) {
    int pv = get(v);
    int pu = get(u);

    if (r[pv] < r[pu]) {
        dsu[pv] = pu;
        for (auto e : edg[pv]) {
            if (get(e.from) != pu) {
                e.w = e.w + d[pv] - d[pu];
                edg[pu].insert(e);
            }
        }

    } else {
        dsu[pu] = pv;
        for (auto e : edg[pu]) {
            if (get(e.from) != pv) {
                e.w = e.w + d[pu] - d[pv];
                edg[pv].insert(e);
            }
        }

        if (r[pv] == r[pu]) {
            ++r[pv];
        }
    }
}

vector<int> used;
long long ans;

bool cycle;
int v_cycle;

void dfs(int v) {
    while (true) {
        v = get(v);
        used[v] = GRAY;

        while (edg[v].begin() != edg[v].end() && get((*edg[v].begin()).from) == v) {
            edg[v].erase(edg[v].begin());
        }

        if (v != get(s)) {
            auto e = *(edg[v].begin());
            ans += e.w + d[v];
            d[v] = -e.w;

            int u = get(e.from);
            assert(v != u);

            if (used[u] == WHITE) {
                dfs(u);
            } else if (used[u] == GRAY) {
                cycle = true;
                v_cycle = e.from;
            }

            if (cycle) {
                v = get(v);
                if (v == get(v_cycle)) {
                    cycle = false;
                    continue;
                } else {
                    un(v, get(e.from));
                }
            }
        }

        used[get(v)] = BLACK;
        break;
    }
}

vector<vector<int>> eeee;

void check(int v) {
    used[v] = true;
    for (auto e : eeee[v]) {
        if (!used[e]) {
            check(e);
        }
    }
}

int main() {
    ios_base::sync_with_stdio(false);
    //freopen("in.txt", "r", stdin);

    cin >> n >> m;

    edg.resize(n);
    eeee.resize(n);

    int b, e, w;
    for (int i = 0; i < m; ++i) {
        cin >> b >> e >> w;
        --b, --e;
        edg[e].insert({b, w});
        eeee[b].push_back(e);
    }

    used.resize(n);
    check(0);

    for (auto i : used) {
        if (!i) {
            cout << "NO";
            return 0;
        }
    }

    dsu.resize(n);
    r.resize(n);
    for (int i = 0; i < n; ++i) {
        dsu[i] = i;
    }

    used.assign(n, 0);
    d.resize(n);

    for (int i = 0; i < n; ++i) {
        if (used[get(i)] == WHITE) {
            dfs(get(i));
        }
    }


    cout << "YES\n" << ans << "\n";
    return 0;
}