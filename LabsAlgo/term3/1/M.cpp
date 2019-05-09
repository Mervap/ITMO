//
// Created by Valery on 07.09.2018.
//

#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <map>

const int cnt = 27;

using namespace std;
int n, m;
vector<vector<int>> e(cnt);
vector<int> color(cnt);
vector<int> ans(cnt);
vector<int> topsort;

bool flag;

void dfs(int i) {
    color[i] = 1;
    for (int j : e[i]) {
        if (color[j] == 0) {
            dfs(j);
        } else if (color[j] == 1) {
            cout << "No";
            exit(0);
        }
    }

    color[i] = 2;
    topsort.push_back(i);
}


int main() {
    cin.tie(0);
    cout.tie(0);

    cin >> n;
    string s, ls;
    for (int i = 0; i < n; ++i) {
        cin >> s;

        if (s.length() != 1) {
            e[0].push_back(s[0] - 'a' + 1);
        }

        if (s.length() < ls.length()) {
            cout << "No";
            return 0;
        }

        if (s.length() == ls.length()) {
            int j = 0;
            while (j < s.length()) {
                if (s[j] != ls[j]) {
                    break;
                }
                ++j;
            }

            if (j == s.length()) {
                cout << "No";
                return 0;
            }

            e[ls[j] - 'a' + 1].push_back(s[j] - 'a' + 1);
        }

        ls = s;
    }

    for (int i = 0; i < cnt; ++i) {
        if (color[i] == 0) {
            dfs(i);
        }
    }

    int l = 0;
    if (topsort[topsort.size() - 1] == 0) {
        cout << "No";
        return 0;
    }

    for (int i = topsort.size() - 1; i >= 0; --i) {
        ans[topsort[i]] = l++;
    }

    cout << "Yes\n";
    for (int i = 1; i < cnt; ++i) {
        if (ans[i] < ans[0]) {
            cout << ans[i] << " ";
        } else {
            cout << ans[i] - 1 << " ";
        }
    }
    return 0;
}