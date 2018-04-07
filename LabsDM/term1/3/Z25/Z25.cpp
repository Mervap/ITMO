#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main() {
    freopen("nextchoose.in", "r", stdin);
    freopen("nextchoose.out", "w", stdout);

    int n, k;
    cin >> n >> k;
    vector<int> a(k);
    vector<bool> was(n + 2);
    was[n + 1] = true;

    for (int i = 0; i < k; ++i) {
        cin >> a[i];
        was[a[i]] = true;
    }

    sort(a.begin(), a.end());

    int i = k - 1;
    while (i >= 0 && was[a[i] + 1]) {
        --i;
    }

    if (i < 0) {
        cout << -1;
        return 0;
    } else {
        was.assign(n + 2, false);
        for (int j = 0; j < i; ++j) {
            cout << a[j] << " ";
            was[a[j]] = true;
        }
        was[a[i]] = false;
        was[a[i] + 1] = true;
        cout << a[i] + 1 << " ";

        int l = a[i] + 1;
        for (int j = i + 1; j < k; ++j) {
            while (was[l]) {
                ++l;
            }
            was[l] = true;
            cout << l << " ";
        }
    }


    return 0;
}
