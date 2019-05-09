#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <map>
#include <cassert>

using namespace std;

struct edge {
    int to, w;
};

struct node {
    int v, p;

    node(int v, int p) : v(v), p(p) {};

    bool operator<(node const &other) const {
        return (p < other.p) || (p == other.p && v < other.v);
    }
};

vector<vector<edge>> edg;
vector<int> d;
set<node> q;

int main() {
    int n, m;
    cin >> n >> m;

    edg.resize(n);

    int a, b, w;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> w;
        --a, --b;
        edg[a].push_back({b, w});
        edg[b].push_back({a, w});
    }

    d.assign(n, 1000000000);
    d[0] = 0;
    for (int i = 0; i < n; ++i) {
        q.insert({i, d[i]});
    }

    while (!q.empty()) {
        auto v = (*q.begin());
        q.erase(q.begin());
        for (auto e : edg[v.v]) {
            if (d[e.to] > d[v.v] + e.w) {
                q.erase({e.to, d[e.to]});
                d[e.to] = d[v.v] + e.w;
                q.insert({e.to, d[e.to]});
            }
        }
    }

    for (auto i : d) {
        cout << i << " ";
    }
    return 0;
}