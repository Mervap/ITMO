#include <iostream>

using namespace std;

int main() {

    freopen("lottery.in", "r", stdin);
    freopen("lottery.out", "w", stdout);

    int n, m;

    cin >> n >> m;

    long double t = 0, p = 1, ans = 0;
    long long last = 0, cost = 0;
    for (int i = 0; i < m; ++i) {
        cin >> t >> cost;
        t = 1 / t;
        ans += (1 - t) * p * last;
        p *= t;
        last = cost;
    }

    ans += p * last;

    printf("%.15Lf", n - ans);
    return 0;
}
