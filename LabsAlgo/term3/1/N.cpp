//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <map>

const int cnt = 27;

using namespace std;
int n, m;
vector<vector<int>> e;
vector<bool> used;
vector<int> color;
vector<pair<int, int>> tout;
int t = 0;
int c = 0;

void dfs1(int v, int k) {
    used[v] = true;

    for (int i = 0; i < n; ++i) {
        if (e[v][i] <= k && !used[i]) {
            dfs1(i, k);
        }
    }

    tout.push_back({t++, v});
}

void dfs2(int v, int k) {
    color[v] = c;

    for (int i = 0; i < n; ++i) {
        if (e[i][v] <= k && !color[i]) {
            dfs2(i, k);
        }
    }
}

bool check(int cnt) {

    t = 0;
    c = 0;
    used.assign(n, false);
    tout.resize(0);
    for (int i = 0; i < n; ++i) {
        if (!used[i]) {
            dfs1(i, cnt);
        }
    }

    sort(tout.begin(), tout.end());
    color.assign(n, 0);
    for (int i = tout.size() - 1; i >= 0; --i) {
        if (!color[tout[i].second]) {
            ++c;
            if(c > 1) {
                return false;
            }
            dfs2(tout[i].second, cnt);
        }
    }

    return true;
}

int main() {
    cin.tie(0);
    cout.tie(0);

    cin >> n;
    e.resize(n, vector<int>(n));
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cin >> e[i][j];
        }
    }

    int l = -1;
    int r = (1 << 30);
    while (r - l > 1) {
        int m = (l + r) / 2;
        if (check(m)) {
            r = m;
        } else {
            l = m;
        }
    }

    cout << r;
    return 0;
}