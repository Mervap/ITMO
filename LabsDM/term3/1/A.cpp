#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

int n;
vector<vector<bool>> edg;
vector<int> ans;

int main() {

    freopen("fullham.in", "r", stdin);
    freopen("fullham.out", "w", stdout);

    cin >> n;
    edg.assign(n, vector<bool>(n));

    for (int i = 0; i < n; ++i) {
        ans.push_back(i);
        char c;
        for (int j = 0; j < i; ++j) {
            cin >> c;
            edg[i][j] = edg[j][i] = (c == '1');
        }
    }

    for (int i = 0; i < n * n; ++i) {
        if (!edg[ans[i]][ans[i + 1]]) {
            auto j = ans.begin() + i + 2;
            while (!(edg[ans[i]][*j] && edg[ans[i + 1]][*(j + 1)])) {
                ++j;
            }
            reverse(ans.begin() + i + 1, j + 1);
        }
        ans.push_back(ans[i]);
    }

    for (int i = n * n; i < ans.size(); ++i) {
        cout << ans[i] + 1 << " ";
    }

    return 0;
}