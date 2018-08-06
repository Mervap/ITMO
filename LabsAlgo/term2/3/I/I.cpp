#include <iostream>
#include <vector>
#include <algorithm>
#include <math.h>

using namespace std;

const int MAX = 1000000;
int k;

struct qq {
    int l, r, n;
};

bool comp(qq a, qq b) {
    if (a.l / k != b.l / k) {
        return a.l < b.l;
    } else {
        return a.r < b.r;
    }

}

int main() {
    freopen("fun.in", "r", stdin);
    freopen("fun.out", "w", stdout);

    int n, m;
    scanf("%d %d", &n, &m);

    k = (int) sqrt(n);

    vector<long long> cnt(MAX + 1);
    vector<long long> a(n);
    vector<long long> ans(m);
    vector<qq> q(m);

    for (size_t i = 0; i < n; ++i) {
        scanf("%lld", &a[i]);
    }

    for (size_t i = 0; i < m; ++i) {
        scanf("%d %d", &q[i].l, &q[i].r);
        --q[i].l;
        --q[i].r;
        q[i].n = i;
    }

    sort(q.begin(), q.end(), comp);

    int l = 1, r = 0;
    long long v, answer = 0;
    for (size_t i = 0; i < m; ++i) {
        while (l > q[i].l) {
            --l;
            v = a[l];
            answer -= (cnt[v] * cnt[v] - (++cnt[v]) * cnt[v]) * v;
        }
        while (r < q[i].r) {
            ++r;
            v = a[r];
            answer -= (cnt[v] * cnt[v] - (++cnt[v]) * cnt[v]) * v;
        }
        while (l < q[i].l) {
            v = a[l];
            ++l;
            answer -= (cnt[v] * cnt[v] - (--cnt[v]) * cnt[v]) * v;
        }
        while (r > q[i].r) {
            v = a[r];
            --r;
            answer -= (cnt[v] * cnt[v] - (--cnt[v]) * cnt[v]) * v;
        }

        ans[q[i].n] = answer;
    }

    for (size_t i = 0; i < m; ++i) {
        printf("%lld\n", ans[i]);
    }

    return 0;
}