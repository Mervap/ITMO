
#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main() {
    freopen("part2num.in", "r", stdin);
    freopen("part2num.out", "w", stdout);


    long long n = 0;
    char c;
    string s = "";

    s = "";
    vector<int> a;
    a.push_back(0);
    while (cin >> c) {
        if (c == '+') {
            a.push_back(atoi(s.c_str()));
            s = "";
            continue;
        }
        s += c;
    }
    a.push_back(atoi(s.c_str()));

    for (int i = 0; i < (int) a.size(); ++i) {
        n += a[i];
    }

    vector<vector<long long> > d;
    d.assign(n + 1, vector<long long>(n + 1));

    for (int i = 0; i <= n; ++i) {
        d[i][i] = 1;
        for (int j = i - 1; j >= 0; --j) {
            if (i - j > 0) {
                d[i][j] += d[i - j][j];
            }
            if (j < n) {
                d[i][j] += d[i][j + 1];
            }
        }
    }

    long long ans = 0;
    for (int i = 1; i < (int) a.size(); ++i) {
        ans += d[n][a[i - 1]] - d[n][a[i]];
        n -= a[i];
    }

    cout << ans;

    return 0;
}
