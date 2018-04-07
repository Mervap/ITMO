#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main() {

    //freopen("input.in", "r", stdin);
    // freopen("input.in", "w", stdout);

    int n;
    cin >> n;

    vector<int> a(n);
    for (int i = 0; i < n; ++i) {
        cin >> a[i];
    }

    vector<vector<long long> > ans;
    ans.assign(n, vector<long long>(n));

    for (int i = 0; i < n; ++i) {
        ans[i][i] = 1;
    }

    long long md = 1000000000;
    for (int i = 0; i < n; ++i) {
        for (int j = i - 1; j >= 0; --j) {
            if (a[j] == a[i]) {
                ans[j][i] = ((ans[j][i - 1] + ans[j + 1][i]) % md + 1) % md;
            } else {
                ans[j][i] = (md + ans[j][i - 1] + ans[j + 1][i] - ans[j + 1][i - 1]) % md;
            }
        }
    }

    cout << ans[0][n - 1] % md;
    return 0;
}
