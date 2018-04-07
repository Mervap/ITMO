#include <iostream>
#include <vector>
#include <fstream>

using namespace std;
int n, m, f;

int main() {
    freopen("binsearch.in", "r", stdin);
    freopen("binsearch.out", "w", stdout);

    cin >> n;
    vector<int> a(n);

    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }

    cin >> m;
    for (int i = 0; i < m; i++) {
        cin >> f;
        int mid, l = -1, r = n;
        while (r - l > 1) {
            mid = (r + l) / 2;
            if (a[mid] < f) {
                l = mid;
            } else {
                r = mid;
            }
        }
        if (a[r] == f) {
            cout << r + 1 << " ";
        } else {
            cout << -1 << " ";
        }

        l = -1;
        r = n;
        while (r - l > 1) {
            mid = (r + l) / 2;
            if (a[mid] <= f) {
                l = mid;
            } else {
                r = mid;
            }
        }
        if (a[l] == f) {
            cout << l + 1 << endl;
        } else {
            cout << -1 << endl;
        }
    }
    return 0;
}
