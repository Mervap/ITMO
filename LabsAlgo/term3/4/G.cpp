#include <iostream>
#include <vector>
#include <queue>

using namespace std;

vector<vector<int>> a;
vector<int> line, column, p, from;

int main() {
    ios_base::sync_with_stdio(false);
    int n;
    cin >> n;

    line.assign(n + 1, 0);
    column.assign(n + 1, 0);
    p.assign(n + 1, -1);
    from.assign(n + 1, -1);
    a.resize(n + 1, vector<int>(n + 1));

    for (int i = 1; i <= n; ++i) {
        for (int j = 1; j <= n; ++j) {
            cin >> a[i][j];
        }
    }

    for (int i = 1; i <= n; ++i) {
        vector<int> cur_column_min(n + 1, 1000000000);
        vector<bool> used(n + 1);
        int cur, min, v, u = 0;
        p[u] = i;
        while (true) {
            used[u] = true;
            cur = p[u];
            min = 1000000000;
            v = -1;

            for (int j = 1; j <= n; ++j) {
                if (!used[j]) {
                    if (a[cur][j] + line[cur] + column[j] < cur_column_min[j]) {
                        cur_column_min[j] = a[cur][j] + line[cur] + column[j];
                        from[j] = u;
                    }

                    if (cur_column_min[j] < min) {
                        min = cur_column_min[j];
                        v = j;
                    }
                }
            }

            for (int j = 0; j <= n; ++j) {
                if (used[j]) {
                    line[p[j]] -= min;
                    column[j] += min;
                } else {
                    cur_column_min[j] -= min;
                }
            }

            u = v;
            if (p[u] == -1) {
                while(u != -1) {
                    v = from[u];
                    p[u] = p[v];
                    u = v;
                }
                break;
            }
        }
    }

    int ans = 0;
    for (int i = 1; i <= n; ++i) {
        ans += a[p[i]][i];
    }

    cout << ans << "\n";
    for (int i = 1; i <= n; ++i) {
        cout << p[i] << " " << i << "\n";
    }

    return 0;
}