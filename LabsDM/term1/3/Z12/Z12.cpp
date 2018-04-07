#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int part(vector<vector<vector<int> > > &ans, int n, int k) {
    vector<vector<int> > c1;
    vector<int> c2;

    if (k == 0 || n == 0 || k > n) {
        return 0;
    }

    if (k == n) {
        c2.push_back(1);
        for (int i = 0; i < n; ++i) {
            c2[0] = i + 1;
            c1.push_back(c2);
        }
        ans.push_back(c1);
        return 1;
    }

    int l = 0;
    int v = 0;
    c2.push_back(n);

    l = part(ans, n - 1, k - 1);
    v += l;
    int s = (int) ans.size();
    for (int j = s - l; j < s; ++j) {
        ans[j].push_back(c2);
    }

    l = part(ans, n - 1, k);
    v += l;
    s = (int) ans.size();
    for (int i = s - l; i < s; ++i) {
        for (int j = 1; j < k; j++) {
            ans.push_back(ans[i]);
            ans[(int) ans.size() - 1][j].push_back(n);
        }
        ans[i][0].push_back(n);
    }
    v += (k - 1) * l;

    return v;

}


int main() {
    freopen("part2sets.in", "r", stdin);
    freopen("part2sets.out", "w", stdout);


    int n, k;
    cin >> n >> k;

    vector<vector<vector<int> > > ans;
    part(ans, n, k);


    for (int i = 0; i < (int) ans.size(); ++i) {
        for (int j = 0; j < (int) ans[i].size(); ++j) {
            sort(ans[i][j].begin(), ans[i][j].end());
            for (int k = 0; k < (int) ans[i][j].size(); ++k) {
                cout << ans[i][j][k] << " ";
            }
            cout << "\n";
        }
        cout << "\n";
    }

    return 0;
}
