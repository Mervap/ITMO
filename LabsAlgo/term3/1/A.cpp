//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>

using namespace std;

vector<vector<int>> gr;
vector<vector<int>> b_gr;
vector<int> was;
vector<int> topsort;
int n, m;
bool flag;

void dfs(int i) {
    was[i] = 1;
    for (int j : gr[i]) {
        if (was[j] == 0) {
            dfs(j);
        } else if (was[j] == 1) {
            flag = true;
        }

        if (flag) {
            return;
        }
    }

    was[i] = 2;
    topsort.push_back(i + 1);
}

int main() {
    freopen("topsort.in", "r", stdin);
    freopen("topsort.out", "w", stdout);
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;
    gr.resize(n);
    b_gr.resize(n);
    was.resize(n);

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        gr[a - 1].push_back(b - 1);
    }

    for (int i = 0; i < n; ++i) {
        if (was[i] == 0) {
            dfs(i);
        }
        if (flag) {
            break;
        }
    }

    if (flag) {
        cout << -1;
    } else {
        for (int i = topsort.size() - 1; i >= 0; --i) {
            cout << topsort[i] << " ";
        }
    }
    return 0;
}