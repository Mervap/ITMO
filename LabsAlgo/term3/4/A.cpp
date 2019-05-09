#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

int n, m, k;
vector<vector<int>> edg;
vector<bool> used;
vector<int> p, pp;
vector<int> was;
vector<bool> inHoll;

void holl_dfs(int v, int type) {
    if (type == 0) {
        inHoll[v] = true;
        for (auto e : edg[v]) {
            if (!was[e]) {
                holl_dfs(e, 1);
            }
        }
    } else {
        was[v] = true;
        holl_dfs(p[v], 0);
    }
}


void holl(vector<int> &ans) {
    was.assign(m, 0);
    inHoll.assign(n, 0);
    for (int i = 0; i < n; ++i) {
        if (pp[i] == -1) {
            holl_dfs(i, 0);
            break;
        }
    }

    for (int i = 0; i < n; ++i) {
        if (inHoll[i]) {
            ans.push_back(i + 1);
        }
    }

    sort(ans.begin(), ans.end());
}

bool dfs(int v) {
    if (used[v]) {
        return false;
    }
    used[v] = true;
    for (auto e : edg[v]) {
        if (p[e] == -1) {
            p[e] = v;
            pp[v] = e;
            return true;
        }
    }

    for (auto e : edg[v]) {
        if (dfs(p[e])) {
            p[e] = v;
            pp[v] = e;
            return true;
        }
    }
    return false;
}

void khun() {
    p.assign(m, -1);
    pp.assign(n, -1);
    int flag = 1;
    while (flag) {
        flag = 0;
        used.assign(n, false);
        for (int i = 0; i < n; ++i) {
            if (pp[i] == -1 && dfs(i)) {
                flag = 1;
            }
        }
    }
}

int main() {
    ios_base::sync_with_stdio(false);

    while (cin >> m >> n >> k) {
        edg.clear();
        edg.resize(n);
        int a, b;
        for (int i = 0; i < k; ++i) {
            cin >> b >> a;
            --a, --b;
            edg[a].push_back(b);
        }

        khun();
        vector<int> ans;
        holl(ans);

        cout << ans.size() << "\n";
        for (auto e : ans) {
            cout << e << " ";
        }
        cout << "\n\n";
    }
}