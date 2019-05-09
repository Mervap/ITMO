//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <set>

using namespace std;

vector<bool> used;
vector<vector<int>> e;
vector<vector<int>> b_edg;
vector<set<int>> new_edg;
vector<pair<int, int>> tout;
vector<int> color;
vector<int> cnt;
int n, m;
int t = 0;
int c = 0;

void dfs1(int v) {
    used[v] = true;

    for (auto e : e[v]) {
        if (!used[e]) {
            dfs1(e);
        }
    }

    tout.push_back({t++, v});
}

void dfs2(int v) {
    color[v] = c;

    for (auto e : b_edg[v]) {
        if (!color[e]) {
            dfs2(e);
        }
    }
}

void dfs3(int v, int cnt) {
    used[v] = true;
    if (cnt == c) {
        cout << "YES";
        exit(0);
    }

    for (auto e : new_edg[v]) {
        if (!used[e]) {
            dfs3(e, cnt + 1);
        }
    }

    used[v] = false;
}

int main() {
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;
    e.resize(n);
    b_edg.resize(n);
    used.resize(n);
    color.resize(n);

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        e[a].push_back(b);
        b_edg[b].push_back(a);
    }

    for (int i = 0; i < n; ++i) {
        if (!used[i]) {
            dfs1(i);
        }
    }

    sort(tout.begin(), tout.end());

    for (int i = tout.size() - 1; i >= 0; --i) {
        if (!color[tout[i].second]) {
            ++c;
            dfs2(tout[i].second);
        }
    }

    new_edg.resize(c);
    cnt.resize(c);
    for (int i = 0; i < n; ++i) {
        int cc = color[i] - 1;
        for (auto e : e[i]) {
            int cce = color[e] - 1;
            if(cce != cc) {
                int s = new_edg[cc].size();
                new_edg[cc].insert(cce);
                cnt[cce] += new_edg[cc].size() - s;
            }
        }
    }

    int s = 0;
    for (int i = 0; i < c; ++i) {
        if (cnt[i] == 0) {
            s = i;
        }
    }

    used.assign(c, 0);
    dfs3(s, 1);

    cout << "NO";
    return 0;
}