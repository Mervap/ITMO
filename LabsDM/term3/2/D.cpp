#include <iostream>
#include <vector>
#include <set>
#include <cmath>

using namespace std;

bool check5(vector<int> const &v, vector<set<int>> &edg) {
    int sum = 0;
    for (int i : v) {
        for (int j : v) {
            if (edg[i].count(j)) {
                ++sum;
            }
        }
    }

    return (sum == (5 * 4));
}

void dfs(int v, vector<set<int>> &edg, vector<int> &color, int c, int &cnt) {
    color[v] = c;
    for (auto e : edg[v]) {
        if (color[e] == -1) {
            dfs(e, edg, color, 1 - c, ++cnt);
        } else if (color[e] != color[v]) {
            ++cnt;
        }
    }
}

bool sub_check33(vector<set<int>> &edg, int n, vector<pair<int, int>> &pairs, vector<bool> &ppairs, int l, int cur,
                 int diff) {
    if (l == pairs.size()) {
        if (cur != diff) {
            return false;
        }

        int cnt = 0;
        vector<int> color(n, -1);

        vector<set<int>> edgs(n);

        for (int i = 0; i < pairs.size(); ++i) {
            if (!ppairs[i] && edg[pairs[i].first].count(pairs[i].second)) {
                edgs[pairs[i].first].insert(pairs[i].second);
                edgs[pairs[i].second].insert(pairs[i].first);
            }
        }

        dfs(0, edgs, color, 0, cnt);

        for (int i = 0; i < n; ++i) {
            if (color[i] == -1) {
                return false;
            }
        }

        return (cnt == 18);
    }

    ppairs[l] = false;
    bool f = sub_check33(edg, n, pairs, ppairs, l + 1, cur, diff);

    if (f) {
        return f;
    }

    ppairs[l] = true;
    return sub_check33(edg, n, pairs, ppairs, l + 1, cur + 1, diff);
}

bool check33(int n, vector<set<int>> &edg) {

    int sum = 0;
    for (auto e : edg) {
        sum += e.size();
        if (e.size() < 3) {
            return false;
        }
    }

    vector<pair<int, int>> pairs;
    vector<bool> ppairs;
    for (int i = 0; i < n; ++i) {
        for (int j = i + 1; j < n; ++j) {
            pairs.push_back({i, j});
            ppairs.push_back(false);
        }
    }

    return sub_check33(edg, n, pairs, ppairs, 0, 0, (sum - 18) / 2);
}

int main() {

    freopen ("planaritycheck.in", "r", stdin);
    freopen ("planaritycheck.out", "w", stdout);

    int t;
    cin >> t;

    string s;
    getline(cin, s);
    for (int i = 0; i < t; ++i) {
        getline(cin, s);
        int n = static_cast<int>(sqrt(2 * s.length())) + 1;

        vector<set<int>> edg(n);
        int cnt = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                if (s[cnt] == '1') {
                    edg[i].insert(j);
                    edg[j].insert(i);
                }
                ++cnt;
            }
        }

        if (n < 5) {
            cout << "YES\n";
            continue;
        }

        if (n == 5) {
            cout << (check5({0, 1, 2, 3, 4}, edg) ? "NO\n" : "YES\n");
            continue;
        }

        if (check33(n, edg)) {
            cout << "NO\n";
            continue;
        }

        bool f = false;

        for (int i = 0; i < n; ++i) {
            bool ff = false;
            int a, b;
            if (edg[i].size() == 2) {
                a = *edg[i].begin();
                b = *(++edg[i].begin());

                if (!edg[a].count(b)) {
                    ff = true;
                    edg[a].insert(b);
                    edg[b].insert(a);
                }
            }

            vector<int> cur;
            for (int j = 0; j < n; ++j) {
                if (j != i) {
                    cur.push_back(j);
                }
            }

            if (check5(cur, edg)) {
                cout << "NO\n";
                f = true;
            }

            if (ff) {
                edg[a].erase(b);
                edg[b].erase(a);
            }

            if (f) {
                break;
            }
        }

        if (f) {
            continue;
        }

        cout << "YES\n";
    }
    return 0;
}