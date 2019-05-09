#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>

using namespace std;

vector<int> prefix(string s) {
    vector<int> tmp;

    tmp.push_back(0);
    for (int i = 1; i < s.length(); ++i) {
        int cur = tmp[i - 1];
        while (cur > 0 && s[cur] != s[i]) {
            cur = tmp[cur - 1];
        }

        tmp.push_back(cur + (s[cur] == s[i]));
    }

    return tmp;
}

int main() {

    freopen("search1.in", "r", stdin);
    freopen("search1.out", "w", stdout);

    string t, s;
    cin >> t >> s;

    auto p = prefix(t + "#" + s);
    vector<int> ans;
    for (int i = t.size() + 1; i < p.size(); ++i) {
        if (p[i] == t.size()) {
            ans.push_back(i - 2 * t.size() + 1);
        }
    }

    cout << ans.size() << "\n";
    for (auto i : ans) {
        cout << i << " ";
    }

    return 0;
}