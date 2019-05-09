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

    freopen("search3.in", "r", stdin);
    freopen("search3.out", "w", stdout);

    string s, t;
    cin >> t >> s;

    auto p = z_func(t + "#" + s);

    reverse(s.begin(), s.end());
    reverse(t.begin(), t.end());
    auto rp = z_func(t + "#" + s);

    vector<int> ans;
    for (int i = t.size() + 1; i < p.size() - t.size() + 1; ++i) {
        if (p[i] + 1 + rp[p.size() - i + 1] >= t.size()) {
            ans.push_back(i - t.size());
        }
    }

    cout << ans.size() << "\n";
    for (auto i : ans) {
        cout << i << " ";
    }

    return 0;
}