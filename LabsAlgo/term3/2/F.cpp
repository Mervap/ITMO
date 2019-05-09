#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <map>
#include <algorithm>
#include <cassert>

using namespace std;

const int INF = 1e9;

struct edge {
    int b, e, w;
};

int n;

vector<edge> edg;
vector<int> p;
vector<int> ans;

int main() {

    freopen("f.in", "r", stdin);

    ios_base::sync_with_stdio(false);
    cin >> n;

    edg.reserve(n + 1);

    int w;
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cin >> w;
            if (w != INF) {
                edg.push_back({i, j, w});
            }
        }
        edg.push_back({n, i, 0});
    }

    vector<int> d(n + 1, INF);
    vector<int> p(n + 1);
    d[n] = 0;
    for (int i = 0; i < n; ++i) {
        for (auto e : edg) {
            if (d[e.b] < INF) {
                if (d[e.e] > d[e.b] + e.w) {
                    d[e.e] = max(-INF, d[e.b] + e.w);
                    p[e.e] = e.b;
                }
            }
        }
    }

    int v = -1;
    for (auto e : edg) {
        if (d[e.b] < INF && d[e.e] > d[e.b] + e.w) {
            v = e.e;
            p[e.e] = e.b;
            break;
        }
    }


    if (v != -1) {
        for (int i = 0; i < n; ++i) {
            v = p[v];
        }

        for (int i = p[v]; i != v; i = p[i]) {
            ans.push_back(i);
        }

        ans.push_back(v);
        ans.push_back(p[v]);

        reverse(ans.begin(), ans.end());
        assert(ans.size() != 0);
        cout << "YES\n" << ans.size() << "\n";

        for (auto i : ans) {
            cout << i + 1 << " ";
        }
    } else {
        cout << "NO";
    }

    return 0;
}