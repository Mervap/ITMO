#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>

using namespace std;

int main() {

    cin.tie(0);
    cout.tie(0);
    ios_base::sync_with_stdio(false);

    string s;
    cin >> s;

    s = "$" + s;
    int n = s.length();

    vector<vector<int>> lcp(n, vector<int>(n)), tmp(n, vector<int>(n));

    for (int i = 1; i < n; ++i) {
        for (int j = 1; j < n; ++j) {
            if (s[i] == s[j] && i != j) {
                lcp[i][j] = lcp[i - 1][j - 1] + 1;
            }
        }
    }

    for (int i = 1; i < n; ++i) {
        int max = 0;
        for (int j = i - 1; j > 0; --j) {
            if (lcp[i][j] > max) {
                for (int k = max + 1; k <= lcp[i][j]; ++k) {
                    ++tmp[j - k + 1][i];
                }

                max = lcp[i][j];
            }
        }
    }

    for (int i = n - 1; i > 0; --i) {
        for (int j = 1; j < n - 1; j++) {
            tmp[i][j + 1] += tmp[i][j];
        }
    }
    for (int i = n - 1; i > 1; --i) {
        for (int j = 1; j < n; j++) {
            tmp[i - 1][j] += tmp[i][j];
        }
    }


    int q;
    cin >> q;
    int l, r;
    for (int i = 0; i < q; ++i) {
        cin >> l >> r;
        int len = (r - l + 1);
        cout << (len * (len + 1)) / 2 - tmp[l][r] << "\n";
    }

    return 0;
}