#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <map>
#include <cassert>

using namespace std;

int n, m, t;
bool f;
int ppp;
vector<vector<int>> edg;
vector<int> d;
vector<int> ans;

void dfs(int i, int p, int r) {
    d[i] = r;

    for (auto e: edg[i]) {
        if (e != p) {
            if (d[e] == -1) {
                dfs(e, i, r + 1);
            } else {
                if ((d[i] + 1 - d[e]) % 2 == 0) {
                    f = true;
                    ppp = e;
                }
            }

            if (f) {
                ans.push_back(i);
                return;
            }
        }
    }
}

int main() {

    cin >> t;
    for (int tt = 0; tt < t; ++tt) {
        cin >> n >> m;
        edg.assign(n, vector<int>(0));

        int a, b;
        for (int i = 0; i < m; ++i) {
            cin >> a >> b;
            --a;
            --b;
            edg[a].push_back(b);
            edg[b].push_back(a);
        }

        for (int j = 0; j < 20; ++j) {

            for (int i = 0; i < n; ++i) {
                random_shuffle(edg[i].begin(), edg[i].end());
            }

            d.assign(n, -1);
            ans.resize(0);

            f = false;
            for (int i = 0; i < n; ++i) {
                if (!f && d[i] == -1) {
                    dfs(i, -1, 0);
                }
            }

            if (f) {
                break;
            }
        }

        if (f) {
            int i = ans.size() - 1;
            while (ans[i] != ppp) {
                --i;
            }

            assert((i + 1) % 2 == 0);
            cout << i + 1 << "\n";
            while (i >= 0) {
                cout << ans[i] + 1 << " ";
                --i;
            }

            cout << "\n";
        } else {
            cout << "-1\n";
        }
    }

    return 0;
}