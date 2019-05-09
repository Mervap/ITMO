#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>

using namespace std;

const long long MOD = 1e9 + 7;

int main() {

    cin.tie(0);
    cout.tie(0);
    ios_base::sync_with_stdio(false);

    int l;
    long long c;

    cin >> l >> c;

    vector<long long> a(1);
    for (int i = 1; i < l; ++i) {
        long long b;
        cin >> b;
        a.push_back(b);
    }

    long long ans = c % MOD;
    for (int i = 1; i < l; ++i) {
        if (a[i] == 0) {
            long long cur = a[i - 1];
            long long c1 = c - 1;
            while (cur > 0) {
                if (a[cur] == 0) {
                    --c1;
                }

                cur = a[cur - 1];
            }
            ans *= c1;
            ans %= MOD;
        }
    }

    cout << ans;

    return 0;
}