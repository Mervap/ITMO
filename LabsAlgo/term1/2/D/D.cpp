#include <iostream>
#include <vector>
#include <fstream>


using namespace std;

int main() {
    freopen("garland.in", "r", stdin);
    freopen("garland.out", "w", stdout);

    int n;
    double a, b, c, l, r, a1, ans = 0, mid;

    cin >> n >> a;
    l = 0;
    r = a;
    double eps = 1e-9;
    while (r - l > eps) {
        mid = (r + l) / 2;
        b = mid;
        a1 = a;
        bool f = false;
        for (int i = 2; i < n; i++) {
            c = 2 * b + 2 - a1;
            if (c - eps <= 0) {
                f = true;
                break;
            }
            a1 = b;
            b = c;
        }
        if (f) {
            l = mid;
        } else {
            r = mid;
            ans = b;
        }
    }

    printf("%.2f", ans);
    return 0;
}
