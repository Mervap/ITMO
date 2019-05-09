#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <map>

using namespace std;

const long long INF = 3 * 1e18;

struct edge {
    int b, e;
    long long w;
};

int n, m, s;

vector<edge> edg;
vector<vector<int>> edg1;
vector<bool> used;

void dfs(int v) {
    used[v] = true;
    for (auto e : edg1[v]) {
        if (!used[e]) {
            dfs(e);
        }
    }
}

int main() {

    //freopen("e.in", "r", stdin);

    ios_base::sync_with_stdio(false);
    cin >> n >> m >> s;
    --s;

    edg.reserve(n);
    edg1.resize(n);

    int a, b;
    long long w;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> w;
        --a, --b;
        edg.push_back({a, b, w});
        edg1[a].push_back(b);
    }

    vector<long long> d(n, INF);
    d[s] = 0;
    for (int i = 0; i < n - 1; ++i) {
        for (auto e : edg) {
            if (d[e.b] < INF) {
                if (d[e.e] > d[e.b] + e.w) {
                    d[e.e] = max(-INF, d[e.b] + e.w);
                }
            }
        }
    }

    vector<int> bad_v;
    for (auto e : edg) {
        if (d[e.b] < INF && d[e.e] > d[e.b] + e.w) {
            bad_v.push_back(e.b);
        }
    }

    used.resize(n);
    for (auto i : bad_v) {
        if (!used[i]) {
            dfs(i);
        }
    }

    for (int i = 0; i < n; ++i) {
        if (used[i]) {
            cout << "-\n";
        } else if (d[i] == INF) {
            cout << "*\n";
        } else {
            cout << d[i] << "\n";
        }
    }
    return 0;
}