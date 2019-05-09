//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <set>
#include <assert.h>
#include <algorithm>

using namespace std;

int n;
vector<int> color_pr;
vector<int> socket;
vector<int> used;
vector<vector<int>> pairs_t;
vector<int> pairs;
vector<vector<int>> edg;

bool dfs(int i) {
    used[i] = 1;
    used[pairs[i]] = 0;

    bool f = true;
    for (auto e : edg[i]) {
        if (used[e] == -1) {
            f &= dfs(e);
        } else if (used[e] == 0) {
            return false;

        }
    }

    return f;
}

int main() {
    cin >> n;
    color_pr.resize(n);
    pairs_t.resize(n);
    socket.resize(2 * n);
    edg.resize(2 * n);
    used.assign(2 * n, -1);

    for (int i = 0; i < n; ++i) {
        cin >> color_pr[i];
    }

    vector<int> all;
    for (int i = 0; i < 2 * n; ++i) {
        cin >> socket[i];
        --socket[i];
        pairs_t[socket[i]].push_back(i);
        all.push_back(i);
    }

    pairs.resize(2 * n);
    for (int i = 0; i < n; ++i) {
        pairs[pairs_t[i][0]] = pairs_t[i][1];
        pairs[pairs_t[i][1]] = pairs_t[i][0];
    }

    for (int i = 0; i < 2 * n; ++i) {
        int prev = (i + 2 * n - 1) % (2 * n);
        if (color_pr[socket[i]] == color_pr[socket[prev]]) {
            edg[i].push_back(pairs[prev]);
            edg[prev].push_back(pairs[i]);
        }
    }

    bool f = true;
    for (int q = 0; q < 30; ++q) {
        random_shuffle(all.begin(), all.end());
        used.assign(2 * n, -1);
        f = true;

        for (int i : all) {
            if (used[i] == -1) {
                f &= dfs(i);
            }
        }

        if (f) {
            break;
        }
    }

    if (!f) {
        cout << "NO";
        return 0;
    }

    cout << "YES\n";
    vector<int> ans(n);
    for (int i = 0; i < 2 * n; ++i) {
        if (used[i] == 1) {
            ans[socket[i]] = i + 1;
        }
    }

    for (auto i : ans) {
        cout << i << " ";
    }
    return 0;
}