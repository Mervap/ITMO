#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int main() {
    freopen("meetings.in", "r", stdin);
    freopen("meetings.out", "w", stdout);

    int n, k;
    cin >> n >> k;

    int l = (1 << n);

    vector<int> a(n), b(n), c(n);
    for (int i = 0; i < n; ++i) {
        cin >> a[i] >> b[i] >> c[i];
    }

    vector<int> dp(l), from(l, -1), kol(l);
    for (int i = 0; i < l; ++i) {
        for (int j = 0; j < n; ++j) {
            if ((i & (1 << j)) != 0) {
                ++kol[i];
                dp[i] += c[j];
                int m = i - (1 << j);
                if ((m == 0 || from[m] != -1) && (k + dp[m] >= a[j]) && (k + dp[m] <= b[j])) {
                    from[i] = j;
                }
            }
        }
    }

    int max = -1;
    k = -1;

    for (int i = 0; i < l; ++i) {
        if (from[i] != -1 && kol[i] > k) {
            k = kol[i];
            max = i;
        }
    }

    if (k == -1 || k == 0) {
        cout << 0;
        return 0;
    }
    cout << k << "\n";

    vector<int> ans;
    while (max != 0) {
        ans.push_back(from[max] + 1);
        max -= (1 << from[max]);
    }

    for (int i = (int) ans.size() - 1; i >= 0; --i) {
        cout << ans[i] << " ";
    }
    return 0;
}
