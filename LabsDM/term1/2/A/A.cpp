#include <iostream>
#include <vector>
#include <algorithm>
#include <fstream>

using namespace std;

int main() {
    freopen("huffman.in", "r", stdin);
    freopen("huffman.out", "w", stdout);

    int n;
    cin >> n;
    vector<long long> a(n + 2), b(n + 2, 100000000000000);

    for (int i = 0; i < n; ++i) {
        cin >> a[i];
    }
    a[n] = 100000000000000;
    a[n + 1] = a[n];

    sort(a.begin(), a.end());

    int i = 0, j = 0;
    long long ans = 0;

    for (int k = 0; k < n - 1; ++k) {
        if (a[i] + a[i + 1] <= a[i] + b[j] && a[i] + a[i + 1] <= b[j] + b[j + 1]) {
            b[k] = a[i] + a[i + 1];
            ans += b[k];
            i += 2;
        } else if (a[i] + b[j] <= a[i] + a[i + 1] && a[i] + b[j] <= b[j] + b[j + 1]) {
            b[k] = a[i] + b[j];
            ans += b[k];
            ++i;
            ++j;
        } else {
            b[k] = b[j] + b[j + 1];
            ans += b[k];
            j += 2;
        }
    }

    cout << ans << endl;
    return 0;
}
