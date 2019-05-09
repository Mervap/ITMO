#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <cassert>

using namespace std;

int n;
vector<vector<bool>> edg;
vector<int> ans;
vector<pair<int, int>> deg;

int main() {

    freopen("chvatal.in", "r", stdin);
    freopen("chvatal.out", "w", stdout);

    cin >> n;
    edg.assign(n, vector<bool>(n));
    deg.resize(n);

    for (int i = 0; i < n; ++i) {
        char c;
        deg[i].second = i;
        for (int j = 0; j < i; ++j) {
            cin >> c;
            edg[i][j] = edg[j][i] = (c == '1');
            ++deg[i].first, ++deg[j].first;
        }
    }

    sort(deg.begin(), deg.end());
    for (int i = deg.size() - 1; i >= 0; --i) {
        ans.push_back(deg[i].second);
    }

    long long cnt = 0;
    for (long long i = 0; cnt <= n * n; ++i, ++cnt) {
        if (!edg[ans[i]][ans[i + 1]]) {
            auto j = ans.begin() + i + 2;
            while (j != ans.end() - 1 && !(edg[ans[i]][*j] && edg[ans[i + 1]][*(j + 1)])) {
                ++j;
            }

            if (j == ans.end() - 1) {
                j = ans.begin() + i + 2;
                while (!edg[ans[i]][*j]) {
                    ++j;
                }
            }

            reverse(ans.begin() + i + 1, j + 1);
        }
        ans.push_back(ans[i]);

        if (i == n) {
            ans.erase(ans.begin(), ans.begin() + i);
            i -= n;
        }
    }

    for (int i = ans.size() - 1; i > ans.size() - n - 1; --i) {
        cout << ans[i] + 1 << " ";
    }

    return 0;
}