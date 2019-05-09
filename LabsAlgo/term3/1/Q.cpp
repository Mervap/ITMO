//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <set>

using namespace std;
int n, m;

struct e {
    int to, num;
    int type;
};

vector<vector<e>> edgs;
vector<int> color;
bool flag;

/* 0 0 | 1
 * 0 1 | 0
 * 1 0 | 0
 * 1 1 | 1
 */

void dfs(int i, int max, int c) {
    color[i] = c;
    for (auto e : edgs[i]) {
        if (e.num <= max) {
            if (color[e.to] == -1) {
                dfs(e.to, max, (e.type ? c : 1 - c));
            } else {
                if (color[e.to] != (e.type ? c : 1 - c)) {
                    flag = false;
                }
            }
        }

        if (!flag) {
            return;
        }
    }
}

bool check(int m) {
    color.assign(n, -1);
    flag = true;
    for (int i = 0; i < n; ++i) {
        if (color[i] == -1) {
            dfs(i, m, 1);
        }
    }

    return flag;
}

int main() {
    cin >> n >> m;
    edgs.resize(n);
    char c;
    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        --a;
        --b;
        edgs[a].push_back({b, i, c == 'T' ? 1 : 0});
        edgs[b].push_back({a, i, c == 'T' ? 1 : 0});
    }

    int l = 0;
    int r = m;

    while (r - l > 1) {
        int mid = (r + l) / 2;
        if (check(mid)) {
            l = mid;
        } else {
            r = mid;
        }
    }

    cout << l + 1;
    return 0;
}