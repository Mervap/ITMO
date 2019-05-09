//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <cassert>

using namespace std;

vector<vector<pair<int, int>>> gr;
vector<bool> used;
vector<int> d;
int n, m, s, t;

void dfs(int i) {
    used[i] = true;
    for (auto j : gr[i]) {
        if (!used[j.first]) {
            dfs(j.first);
        }

        d[i] = min(d[i], d[j.first] + j.second);
    }

}

int main() {
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m >> s >> t;
    --s;
    --t;
    gr.resize(n);
    used.resize(n);
    d.assign(n, INT_MAX - 10000);
    d[s] = 0;

    int a, b, w;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> w;
        gr[b - 1].push_back({a - 1, w});
    }

    dfs(t);

    if (!used[s]) {
        cout << "Unreachable";
    } else {
        cout << d[t];
    }
    return 0;
}