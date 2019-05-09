#include <iostream>
#include <vector>
#include <set>
#include <cassert>

using namespace std;

struct edge {
    int b;
    int e;
    double x;
    double y;

    bool operator<(edge const &other) const {
        return b < other.b || (b == other.b && e < other.e);
    }
};

vector<vector<int>> edg_of_edg;
vector<int> color;

void dfs(int i, int c) {
    color[i] = c;

    for (auto e : edg_of_edg[i]) {
        if (color[e] == -1) {
            dfs(e, 1 - c);
        } else if (color[e] == color[i]) {
            cout << "NO";
            exit(0);
        }
    }
}

int main() {

    int n, m;
    cin >> n >> m;

    vector<edge> edg;
    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        assert(a != b);
        edg.push_back({a, b});
    }

    vector<int> cycle(n);
    for (int i = 0; i < n; ++i) {
        cin >> a;
        --a;
        cycle[a] = i;
    }

    for (int i = 0; i < m; ++i) {
        if (cycle[edg[i].e] < cycle[edg[i].b]) {
            std::swap(edg[i].e, edg[i].b);
        }
    }

    set<edge> v;
    for (auto e : edg) {
        v.insert(e);
    }

    assert(v.size() == m);

    for (int i = 0; i < m; ++i) {
        if (cycle[edg[i].e] - 1 == cycle[edg[i].b]) {
            edg[i].x = cycle[edg[i].b] + 0.5;
            edg[i].y = 0;
        } else {
            edg[i].x = (cycle[edg[i].b] + cycle[edg[i].e]) / 2.0;
            edg[i].y = (cycle[edg[i].e] - cycle[edg[i].b]) / 2.0;
            assert(edg[i].y > 0);
        }
    }

    edg_of_edg.resize(m);
    for (int i = 0; i < m; ++i) {
        if (edg[i].y > 0) {
            for (int j = 0; j < m; ++j) {
                if (edg[j].y > 0) {
                    int ib = cycle[edg[i].b];
                    int ie = cycle[edg[i].e];
                    int jb = cycle[edg[j].b];
                    int je = cycle[edg[j].e];

                    if ((ib < jb && jb < ie && ie < je) ||
                        (jb < ib && ib < je && je < ie)) {
                        edg_of_edg[i].push_back(j);
                        edg_of_edg[j].push_back(i);
                    }
                }
            }
        }
    }

    color.assign(m, -1);
    for (int i = 0; i < m; ++i) {
        if (edg[i].y > 0 && color[i] == -1) {
            dfs(i, 0);
        }
    }

    cout << "YES\n";
    for (int i = 0; i < n; ++i) {
        cout << cycle[i] << " 0 ";
    }

    for (int i = 0; i < m; ++i) {
        cout << "\n" << edg[i].x << " ";

        if (color[i] < 1) {
            cout << edg[i].y;
        } else {
            cout << -edg[i].y;
        }
    }
    return 0;
}