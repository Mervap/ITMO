#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

int n, m;
vector<bool> indep, indep1;
vector<int> cnt;

int main() {
    ios_base::sync_with_stdio(false);

    freopen("check.in", "r", stdin);
    freopen("check.out", "w", stdout);

    cin >> n >> m;
    indep.resize(1 << n);
    indep1.resize(1 << n);
    cnt.resize(1 << n);

    for (int i = 0; i < m; ++i) {
        int mi;
        cin >> mi;

        int a = 0, cur;
        for (int j = 0; j < mi; ++j) {
            cin >> cur;
            a |= (1 << (cur - 1));
        }

        indep[a] = true;
        cnt[a] = mi;
    }

    if (!indep[0]) {
        cout << "NO";
        return 0;
    }

    for (int i = 0; i < (1 << n); ++i) {
        if (!indep[i]) {
            continue;
        }

        indep1[i] = true;
        for (int j = 0; j < n; ++j) {
            indep1[i] = indep1[i] && indep1[i & (~(1 << j))];
        }
    }

    for (int i = 0; i < (1 << n); ++i) {
        if (indep[i] != indep1[i]) {
            cout << "NO";
            return 0;
        }
    }

    for (int i = 0; i < (1 << n); ++i) {
        for (int j = 0; j < (1 << n); ++j) {
            if (indep[i] && indep[j]) {
                if (cnt[i] > cnt[j]) {
                    bool f = false;
                    for (int k = 0; k < n; ++k) {
                        if (((i & (1 << k)) != 0) && ((j & (1 << k)) == 0)) {
                            if (indep[j | (1 << k)]) {
                                f = true;
                                break;
                            }
                        }
                    }

                    if (!f) {
                        cout << "NO";
                        return 0;
                    }
                }
            }
        }
    }

    cout << "YES";
}