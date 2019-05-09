#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

const uint64_t MOD = 1'000'000'007;

int main() {

    int m, k;

    cin >> k >> m;

    vector<uint64_t> c(k);

    for (int i = 0; i < k; ++i) {
        cin >> c[i];
    }

    vector<uint64_t> ans(m + 1);
    ans[0] = 1;


    vector<uint64_t> sums(m + 1);
    sums[0] = 1;

    for (int i = 1; i <= m; ++i) {
        for (int j = 0; j < k; ++j) {
            if (i >= c[j]) {
                ans[i] += sums[i - c[j]];
                ans[i] %= MOD;
            }
        }

        for(int j = 0; j <= i; ++j) {
            sums[i] += (ans[j] * ans[i - j]) % MOD;
            sums[i] %= MOD;
        }
    }

    for (int i = 1; i <= m; ++i) {
        cout << ans[i] << " ";
    }

    return 0;
}