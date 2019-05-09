#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector<bool> used;
vector<vector<int>> edg;
vector<vector<int>> b_edg;
vector<pair<int, int>> tout;
vector<int> color;
int t = 0;
int c = 0;

void dfs1(int v) {
    used[v] = true;

    for (auto e : edg[v]) {
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

int main() {
    int n;
    iostream::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);

    cin >> n;
    edg.resize(2 * n);
    b_edg.resize(2 * n);
    used.resize(2 * n);
    color.resize(2 * n);

    int mi, a;
    for (int i = 0; i < n; ++i) {
        cin >> mi;
        for (int j = 0; j < mi; ++j) {
            cin >> a;
            --a;
            edg[n + a].push_back(i);
            b_edg[i].push_back(n + a);
        }
    }

    for (int i = 0; i < n; ++i) {
        cin >> a;
        --a;
        edg[i].push_back(n + a);
        b_edg[n + a].push_back(i);

        auto it = edg[n + a].begin();
        while (*it != i) {
            ++it;
        }
        edg[n + a].erase(it);

        it = b_edg[i].begin();
        while (*it != n + a) {
            ++it;
        }
        b_edg[i].erase(it);
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

    for (int i = 0; i < n; ++i) {
        vector<int> ans;
        ans.push_back(edg[i][0] + 1 - n);

        for (auto e : b_edg[i]) {
            if (color[e] == color[i]) {
                ans.push_back(e + 1 - n);
            }
        }

        cout << ans.size() << " ";
        for (auto j : ans) {
            cout << j << " ";
        }

        cout << "\n";
    }

    return 0;
}