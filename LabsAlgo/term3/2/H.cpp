#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

struct edge {
    int w;
    int b;
    int e;

    bool operator<(edge const &other) {
        return w < other.w;
    }
};

vector<edge> edg;
vector<int> p;
vector<int> r;

int get(int v) {
    if (p[v] == v) {
        return v;
    } else {
        return get(p[v]);
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


int main() {
    int n, m;

    cin >> n >> m;
    int a, b, w;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> w;
        edg.push_back({w, --a, --b});
    }

    sort(edg.begin(), edg.end());

    p.resize(n);
    r.resize(n);
    for (int i = 0; i < n; ++i) {
        p[i] = i;
    }

    int ans = 0;
    for (auto e : edg) {
        if (get(e.b) != get(e.e)) {
            un(e.b, e.e);
            ans += e.w;
        }
    }

    cout << ans;
    return 0;
}