//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>

using namespace std;

vector<vector<int>> e;
vector<int> used;
vector<int> ans;
int n, m;
int flag = 0;
int st = -1;

void find_circle(int i) {
    used[i] = 1;
    for (int j : e[i]) {
        if (used[j] == 0) {
            find_circle(j);
        } else if (used[j] == 1) {
            flag = 1;
            st = j;
        }

        if (flag > 0) {
            if (flag == 1) {
                ans.push_back(i);
                if (i == st) {
                    flag = 2;
                }
            }
            return;
        }
    }

    used[i] = 2;
}

int main() {

    freopen("cycle.in", "r", stdin);
    freopen("cycle.out", "w", stdout);
    cin.tie(0);
    cout.tie(0);

    cin >> n >> m;

    e.resize(n);
    used.resize(n);

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        e[a].push_back(b);
    }


    for (int i = 0; i < n; ++i) {
        if (!used[i]) {
            find_circle(i);
        }

        if (flag > 0) {
            break;
        }
    }

    if (flag == 0) {
        cout << "NO";
    } else {
        cout << "YES\n";
        for (int i = ans.size() - 1; i >= 0; --i) {
            cout << ans[i] + 1 << " ";
        }
    }

    return 0;
}