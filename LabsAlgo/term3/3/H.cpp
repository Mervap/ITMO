#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <cassert>

using namespace std;

int n, m;
vector<multiset<int>> edg;
vector<int> cycle;
vector<int> addedEdg;

void euler(int v) {
    assert(v >= 0);
    while (!edg[v].empty()) {
        int u = *edg[v].begin();
        if (u < 0) {
            u = -u;
            edg[u].erase(edg[u].find(-v));
        } else {
            edg[u].erase(edg[u].find(v));
        }
        edg[v].erase(edg[v].begin());
        euler(u);
    }
    cycle.push_back(v);
}

int main() {
    ios_base::sync_with_stdio(false);
    cin >> n >> m;

    edg.resize(n + 1);
    addedEdg.assign(n + 1, -1);
    for (int i = 0; i < m; ++i) {
        int b, e;
        cin >> b >> e;
        edg[b].insert(e);
        edg[e].insert(b);
    }

    int s = 1;
    vector<int> odd(0);
    for (int i = 1; i <= n; ++i) {
        if (edg[i].size() % 2 == 1) {
            odd.push_back(i);
            s = i;
        }
    }

    if (!odd.empty()) {
        for (int i = 0; i < odd.size() - 1; i += 2) {
            int u = odd[i];
            int v = odd[i + 1];
            addedEdg[u] = v;
            addedEdg[v] = u;
            edg[u].insert(-v);
            edg[v].insert(-u);
        }
    }

    euler(s);

    reverse(cycle.begin(), cycle.end());

    if (odd.empty()) {
        cout << "1\n";
    } else {
        cout << odd.size() / 2 << "\n";
    }

    vector<int> ans;
    for (int i = 0; i < cycle.size(); ++i) {
        int next = (i + 1) % cycle.size();
        ans.push_back(cycle[i]);
        if (addedEdg[cycle[i]] == cycle[next]) {
            addedEdg[cycle[i]] = -1;
            addedEdg[cycle[next]] = -1;
            if (ans.size() >= 2) {
                for (auto e : ans) {
                    cout << e << " ";
                }
                cout << "\n";
            }
            ans.clear();
        }
    }

    if (!ans.empty()) {
        for (auto e : ans) {
            cout << e << " ";
        }
    }

    return 0;
}