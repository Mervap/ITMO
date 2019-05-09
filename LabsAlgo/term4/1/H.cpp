#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <queue>

using namespace std;

int main() {

    freopen("substrcmp.in", "r", stdin);
    freopen("substrcmp.out", "w", stdout);

    string s;
    cin >> s;
    int n = s.length();

    unsigned long long p = 31;
    vector<unsigned long long> h(n + 1);
    vector<unsigned long long> pp(n);

    pp[0] = 1;
    for (int i = 1; i < n; ++i) {
        pp[i] = pp[i - 1] * p;
    }

    for (int i = 1; i <= s.length(); ++i) {
        h[i] = h[i - 1] + (s[i - 1] - 'a' + 1) * pp[i - 1];
    }

    int t;
    cin >> t;

    for (int i = 0; i < t; ++i) {
        int a, b, c, d;
        cin >> a >> b >> c >> d;

        if (b - a != d - c) {
            cout << "No\n";
            continue;
        }

        unsigned long long h1 = h[b] - h[a - 1];
        unsigned long long h2 = h[d] - h[c - 1];

        if (a < c) {
            h1 *= pp[c - a];
        } else {
            h2 *= pp[a - c];
        }

        cout << (h1 == h2 ? "Yes\n" : "No\n");
    }

    return 0;
}