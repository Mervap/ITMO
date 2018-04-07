#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main() {
    freopen("perm2num.in", "r", stdin);
    freopen("perm2num.out", "w", stdout);


    long long n;
    cin >> n;

    vector<long long> fact(n + 1);
    vector<bool> was(n + 1);

    fact[0] = 1;
    for (int i = 1; i < n; ++i) {
        fact[i] = fact[i - 1] * i;
    }

    long long ans = 0;
    int z;
    for (int i = 0; i < n; ++i) {
        cin >> z;

        long long l = 0;
        for (int j = 1; j <= n; ++j) {
            if (was[j] == false) {
                ++l;
                if (j == z) {
                    was[j] = true;
                    ans += (l - 1) * fact[n - i - 1];
                }
            }
        }
    }

    cout << ans;

    return 0;
}
