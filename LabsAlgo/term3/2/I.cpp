#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

const int INF = 1000000001;

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

    int ans = 2 * INF;
    for (int i = 0; i < m; ++i) {
        p.assign(n, 0);
        r.assign(n, 0);
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }

        int cnt = 0;
        int min = INF;
        int max = -INF;
        for (int j = i; j < m; ++j) {
            auto e = edg[j];
            if (get(e.b) != get(e.e)) {
                un(e.b, e.e);
                min = std::min(min, e.w);
                max = std::max(max, e.w);
                ++cnt;
            }
        }

        if (cnt == n - 1) {
            ans = std::min(ans, max - min);
        }
    }

    if(ans == 2 * INF) {
        cout << "NO";
    } else {
        cout << "YES\n" << ans;

    }
    return 0;
}