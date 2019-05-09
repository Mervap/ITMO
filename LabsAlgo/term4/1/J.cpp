#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>

using namespace std;

vector<int> reversed_z_func(string s, int n) {
    int l = n - 1, r = n - 1;

    vector<int> z(n);
    for (int i = n - 2; i >= 0; --i) {
        if (i >= r) {
            z[i] = min(i - r + 1, z[n - 1 - (l - i)]);
        }

        while (i - z[i] >= 0 && s[i - z[i]] == s[n - 1 - z[i]]) {
            ++z[i];
        }

        if (i - z[i] + 1 < r) {
            r = i - z[i] + 1;
            l = i;
        }
    }

    return z;
}

int main() {

    freopen("keepcounted.in", "r", stdin);
    freopen("keepcounted.out", "w", stdout);

    string s;
    cin >> s;

    int ans = 0;
    for (int i = 1; i <= s.length(); ++i) {
        auto tmp = reversed_z_func(s, i);
        ans += i - *max_element(tmp.begin(), tmp.end());
        cout << ans << "\n";
    }

    return 0;
}