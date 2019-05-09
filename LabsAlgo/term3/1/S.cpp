//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <set>

using namespace std;
int n, ls;
vector<int> l;
vector<vector<int>> tec;
vector<vector<int>> tree;
set<pair<int, int>> ans;
int tt = 1;

void dfs(int v, int p) {
    while (tt < ls) {
        if (l[tt] == p) {
            tec[p].push_back(v);
            tree[p].push_back(v);
            for (auto t : tree[v]) {
                if (t > v) {
                    tec[p].push_back(t);
                }
                tree[p].push_back(t);
            }
            ++tt;
            return;
        } else {
            ++tt;
            dfs(l[tt - 1], v);
        }
    }
}


int main() {
    cin >> n >> ls;
    l.resize(ls);
    tec.resize(n + 1);
    tree.resize(n + 1);
    for (int i = 0; i < ls; ++i) {
        cin >> l[i];
    }

    dfs(l[0], -1);
    for (int i = 1; i <= n; ++i) {
        for (auto t : tec[i]) {
            ans.insert({min(i, t), max(i, t)});
            //ans.insert({i, t});
        }
    }
    cout << ans.size() << "\n";
    for (auto i : ans) {
        cout << i.first << " " << i.second << "\n";
    }
    return 0;
}