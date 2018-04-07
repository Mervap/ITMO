#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main() {
    freopen("num2perm.in", "r", stdin);
    freopen("num2perm.out", "w", stdout);


    long long n, k;
    cin >> n >> k;

    vector<long long> fact(n + 1);
    vector<bool> was(n + 1);

    fact[0] = 1;
    for (int i = 1; i < n; ++i) {
        fact[i] = fact[i - 1] * i;
    }

    long long z;
    for (int i = 1; i <= n; ++i) {
        z = k / (fact[n - i]) + 1;
        k = k % (fact[n - i]);

        int l = 0;
        for (int j = 0; j < n; ++j) {
            if (was[j] == false) {
                ++l;
                if (l == z) {
                    was[j] = true;
                    cout << j + 1 << " ";
                }
            }
        }
    }

    return 0;
}
