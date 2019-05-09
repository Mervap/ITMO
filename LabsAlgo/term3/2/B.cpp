#include <iostream>
#include <vector>
#include <ctime>

using namespace std;

const long long INF = 1000000000000000;

vector<vector<int>> edg;
vector<long long> d;
vector<bool> used;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n, s, t;
    cin >> n >> s >> t;

    edg.assign(n, vector<int>(n));

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cin >> edg[i][j];
        }
    }

    d.assign(n, INF);
    used.resize(n);
    d[s - 1] = 0;

    for (int i = 0; i < n; ++i) {
        int v = -1;
        for (int j = 0; j < n; ++j) {
            if (!used[j] && (v == -1 || d[j] < d[v])) {
                v = j;
            }
        }

        if (d[v] == INF || v == t - 1) {
            break;
        }

        used[v] = true;
        for (int j = 0; j < n; ++j) {
            if (edg[v][j] != -1 && d[j] > d[v] + edg[v][j]) {
                d[j] = d[v] + edg[v][j];
            }
        }
    }

    cout << (d[t - 1] == INF ? -1 : d[t - 1]);

    return 0;
}