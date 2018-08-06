#include <iostream>

using namespace std;

int main() {

    freopen("shooter.in", "r", stdin);
    freopen("shooter.out", "w", stdout);

    int n, m, k;

    cin >> n >> m >> k;

    long double p, q, sum = 0, t;
    for (int i = 0; i < n; ++i) {
        cin >> p;
        p = 1 - p;
        q = 1;
        for (int j = 0; j < m; ++j) {
            q *= p;
        }

        if (i + 1 == k) {
            t = q;
        }

        sum += q;
    }

    printf("%.15Lf", t / sum);
    return 0;
}
