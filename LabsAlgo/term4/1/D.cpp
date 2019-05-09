#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>

using namespace std;

vector<int> z_func(string s) {
    int l = 0, r = 0, n = s.length();

    vector<int> z(s.length());
    for (int i = 1; i < n; ++i) {
        if (i <= r) {
            z[i] = min(r - i + 1, z[i - l]);
        }

        while (i + z[i] < n && s[i + z[i]] == s[z[i]]) {
            ++z[i];
        }

        if (i + z[i] - 1 > r) {
            r = i + z[i] - 1;
            l = i;
        }
    }

    return z;
}

int main() {

    freopen("z.in", "r", stdin);
    freopen("z.out", "w", stdout);

    string s;
    cin >> s;

    auto ans = z_func(s);
    for (int i = 1; i < ans.size(); ++i) {
        cout << ans[i] << " ";
    }

    return 0;
}