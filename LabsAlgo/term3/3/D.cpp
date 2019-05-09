#include <iostream>
#include <vector>
#include <set>

using namespace std;

int n, m;
vector<vector<bool>> edg;
vector<vector<int>> edgMatching;
vector<bool> used;
set<int> rm;
set<int> lp;
vector<int> p;

bool matching(int v) {
    if (used[v]) {
        return false;
    }
    used[v] = true;
    for (int i = 0; i < m; ++i) {
        if (edg[v][i]) {
            if (p[i] == -1 || matching(p[i])) {
                p[i] = v;
                return true;
            }
        }
    }
    return false;
}

void dfs(int v) {
    used[v] = true;
    if (v < n) {
        lp.insert(v);
    } else {
        rm.erase(v);
    }

    for (auto e : edgMatching[v]) {
        if (!used[e]) {
            dfs(e);
        }
    }
}

int main() {

    ios_base::sync_with_stdio(false);
    int t;
    cin >> t;

    for (int tt = 0; tt < t; ++tt) {
        cin >> n >> m;

        edg.assign(n, vector<bool>(m, true));
        int b;
        for (int i = 0; i < n; ++i) {
            while (cin >> b && b != 0) {
                --b;
                edg[i][b] = false;
            }
        }

        p.assign(m, -1);
        for (int i = 0; i < n; ++i) {
            used.assign(n, false);
            matching(i);
        }


        set<int> bad;
        for (int i = 0; i < n; ++i) {
            bad.insert(i);
        }

        edgMatching.assign(n + m, vector<int>());
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (edg[i][j]) {
                    if (i == p[j]) {
                        edgMatching[n + j].push_back(i);
                    } else {
                        edgMatching[i].push_back(n + j);
                    }
                }
            }
        }


        rm.clear();
        lp.clear();
        for (int i = 0; i < m; ++i) {
            if (p[i] != -1) {
                bad.erase(p[i]);
            }
        }

        for (int i = 0; i < m; ++i) {
            rm.insert(i + n);
        }

        used.assign(n + m, false);
        for (auto v : bad) {
            if (!used[v]) {
                dfs(v);
            }
        }

        cout << lp.size() + rm.size() << "\n";
        cout << lp.size() << " " << rm.size() << "\n";
        for (auto l : lp) {
            cout << l + 1 << " ";
        }

        cout << "\n";

        for (auto r : rm) {
            cout << r + 1 - n << " ";
        }

        cout << "\n";
    }

    return 0;
}