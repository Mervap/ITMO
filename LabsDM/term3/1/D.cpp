#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <cassert>

using namespace std;

int n;
vector<vector<bool>> edg;
vector<int> ans;
vector<int> tmp;

int main() {

    freopen("guyaury.in", "r", stdin);
    freopen("guyaury.out", "w", stdout);

    cin >> n;
    edg.assign(n, vector<bool>(n));

    for (int i = 0; i < n; ++i) {
        char c;
        for (int j = 0; j < i; ++j) {
            cin >> c;
            edg[i][j] = (c == '1');
            edg[j][i] = (c == '0');
        }
        tmp.push_back(i);
    }


    while (true) {
        ans.clear();
        random_shuffle(tmp.begin(), tmp.end());
        for (int i = 0; i < n; ++i) {
            bool f = false;
            for (int j = 0; j < i; ++j) {
                if (!edg[ans[j]][tmp[i]]) {
                    ans.insert(ans.begin() + j, tmp[i]);
                    f = true;
                    break;
                }
            }

            if (!f) {
                ans.push_back(tmp[i]);
            }
        }

        if (edg[ans[ans.size() - 1]][ans[0]]) {
            break;
        }
    }

    for (auto i : ans) {
        cout << i + 1 << " ";
    }
    return 0;
}