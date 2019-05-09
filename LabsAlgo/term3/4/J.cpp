#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

struct edge {
    int to;
    int w;
};

int n, k;
vector<vector<edge>> edg;
vector<bool> used;
vector<int> p, pp;
vector<int> was;
vector<bool> inHoll;

bool dfs(int v, int w_max) {
    if (used[v]) {
        return false;
    }
    used[v] = true;
    for (auto e : edg[v]) {
        if (e.w < w_max) {
            continue;
        }
        if (p[e.to] == -1) {
            p[e.to] = v;
            pp[v] = e.to;
            return true;
        }
    }

    for (auto e : edg[v]) {
        if (e.w < w_max) {
            continue;
        }
        if (dfs(p[e.to], w_max)) {
            p[e.to] = v;
            pp[v] = e.to;
            return true;
        }
    }
    return false;
}

void khun(int w_max) {
    p.assign(n, -1);
    pp.assign(n, -1);
    int flag = 1;
    while (flag) {
        flag = 0;
        used.assign(n, false);
        for (int i = 0; i < n; ++i) {
            if (pp[i] == -1 && dfs(i, w_max)) {
                flag = 1;
            }
        }
    }
}

int main() {
    ios_base::sync_with_stdio(false);

    cin >> n;
    edg.resize(n);
    int w, t_min = 1000001, t_max = -1;
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cin >> w;
            t_min = std::min(t_min, w);
            t_max = std::max(t_max, w);
            edg[i].push_back({j, w});
        }
    }

    ++t_max;
    while (t_max - t_min > 1) {
        int t_mid = (t_max + t_min) / 2;
        khun(t_mid);

        bool f = true;
        for (int i = 0; i < n; ++i) {
            if (pp[i] == -1) {
                f = false;
                break;
            }
        }

        if (f) {
            t_min = t_mid;
        } else {
            t_max = t_mid;
        }
    }

    cout << t_min;

}