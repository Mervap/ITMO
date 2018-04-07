#include <iostream>
#include <vector>

using namespace std;
unsigned long long ans = 0;

long long *a;
long long k;
unsigned int cur = 0, a1, b1, c, d;

unsigned int nextRand24() {
    cur = cur * a1 + b1;
    return cur >> 8;
}

unsigned int nextRand32() {
    c = nextRand24();
    d = nextRand24();
    return (c << 8) ^ d;
}

void msort(int l, int r) {
    if (l >= r) {
        return;
    }
    int mid = (r + l) / 2;
    msort(l, mid);
    msort(mid + 1, r);

    int i = l, j = mid + 1;
    int kol = 0;

    while (j <= r) {
        while (i <= mid && a[j] - a[i] >= k) {
            kol++;
            i++;
        }
        ans += (unsigned long long) kol;
        j++;
    }

    long long *b = new long long[r - l + 1];
    int j1, j2;
    j1 = l;
    j2 = mid + 1;
    for (int i = l; i <= r; i++) {
        if (j1 <= mid && (j2 > r || (a[j1] <= a[j2]))) {
            b[i - l] = a[j1];
            j1++;
        } else {
            b[i - l] = a[j2];
            j2++;
        }
    }

    for (int i = l; i <= r; i++) {
        a[i] = b[i - l];
    }
    delete[]b;
}

int main() {
    freopen("bigseg.in", "r", stdin);
    freopen("bigseg.out", "w", stdout);

    int n;
    cin >> n >> k >> a1 >> b1;

    a = new long long[n];

    a[0] = (int) nextRand32();
    if (a[0] >= k) {
        ans++;
    }
    for (int i = 1; i < n; i++) {
        a[i] = a[i - 1] + (int) nextRand32();
        if (a[i] >= k) {
            ans++;
        }
    }

    msort(0, n - 1);

    printf("%llu", ans);
    return 0;
}
