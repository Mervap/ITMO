#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main() {

    freopen("bridge.in", "r", stdin);
    freopen("bridge.out", "w", stdout);

    int x, a, y, b, n;
    cin >> x >> a >> y >> b >> n;

    int l = min(a, b);
    int r = x * a + y * b + 1;

    while (r - l > 1) {
        int mid = (r + l) / 2;
        vector<vector<int> > dp;
        dp.assign(x + 1, vector<int>(y + 1));

        for (int i = 0; i <= x; ++i) {
            for (int j = 0; j <= y; ++j) {
                for (int k = 0; k <= i; ++k) {
                    int k1 = (mid - k * a) / b;
                    if ((mid - k * a) % b > 0) {
                        k1++;
                    }
                    k1 = max(k1, 0);
                    if (k1 > j) {
                        continue;
                    }
                    dp[i][j] = max(dp[i][j], dp[i - k][j - k1] + 1);
                    //   cout << dp[i][j] << " " << i << " " << j << " " << k << " "<< k1 << "\n";
                }
            }
        }

        // cout << dp[x][y] << " " << mid << "\n";
        if (dp[x][y] >= n) {
            l = mid;
        } else {
            r = mid;
        }
    }
    cout << l << endl;
    return 0;
}
