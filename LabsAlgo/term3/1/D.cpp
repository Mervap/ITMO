//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <cassert>

using namespace std;

vector<vector<int>> gr;
vector<bool> used;
vector<vector<int>> d;
int n, m, s;

void dfs(int i) {
    used[i] = true;
    for (auto j : gr[i]) {
        if (!used[j]) {
            dfs(j);
        }

        d[0][i] |= !d[1][j];
        d[1][i] |= !d[0][j];
    }

}

int main() {
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m >> s;
    --s;
    gr.resize(n);
    used.resize(n);
    d.assign(2, vector<int>(n));

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        gr[a - 1].push_back(b - 1);
    }

    dfs(s);

    if (d[0][s]) {
        cout << "First player wins";
    } else {
        cout << "Second player wins";
    }
    return 0;
}