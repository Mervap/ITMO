#include <iostream>

using namespace std;

int main() {

    freopen("exam.in", "r", stdin);
    freopen("exam.out", "w", stdout);

    double n;
    int m;
    long long ans = 0;

    scanf("%d%lf", &m, &n);

    long long p, q;
    for (int i = 0; i < m; ++i) {
        scanf("%lld%lld", &p, &q);
        ans += p * q;
    }

    double ans1 = (double) ans;

    printf("%.9lf", ans1 / n / 100);
    return 0;
}
