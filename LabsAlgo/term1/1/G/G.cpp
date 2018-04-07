#include <iostream>
#include <fstream>
#include <stdlib.h>

using namespace std;

long f, l, n, m, k, s;
long long k1, k2, k3, sum, x;

void push(long long k);
void pop();

long long *a;
long long *b;
long long *cur;

long long c = 1;

int main() {
    freopen("queuemin2.in", "r", stdin);
    freopen("queuemin2.out", "w", stdout);

    f = l = 0;

    a = (long long *) malloc(1000 * sizeof(long long));
    s = 1000;
    for (int i = 1; i <= 32; i++) {
        c *= 2;
    }


    ios_base::sync_with_stdio(false);
    cout.tie(0);
    cin.tie(0);

    cin >> n >> m >> k;
    cin >> k1 >> k2 >> k3;

    cur = (long long *) malloc(n * sizeof(long long));

    for (int i = 0; i < k; i++) {
        cin >> x;
        cur[i] = x;
    }

    for (int i = k; i < n; i++) {
        long long z = ((((k1 * cur[i - 2]) % c) + ((k2 * cur[i - 1]) % c)) % c + k3) % c;
        if (z > c / 2 - 1) {
            z = z - c;
        }
        if (z < -(c / 2)) {
            z = z + c;
        }
        cur[i] = z;
    }

    for (int i = 0; i < m; i++) {
        push(cur[i]);
    }
    sum = a[f];

    for (int i = m; i < n; i++) {
        if (l > f && a[f] == cur[i - m]) {
            pop();
        }
        push(cur[i]);
        sum = sum + a[f];
    }

    cout << sum;

    return 0;
}

void push(long long k) {
    while (l > f && a[l - 1] > k) {
        l--;
    }
    if (l == s) {
        s = (l - f + 1) * 2;
        b = a;
        a = (long long *) malloc(s * sizeof(long long));
        for (int i = f; i < l; i++) {
            a[i - f] = b[i];
        }

        l -= f;
        f = 0;
        free(b);

        a[l] = k;
        l++;
    } else {
        a[l] = k;
        l++;
    }


}

void pop() {
    f++;
    if (l - f <= s / 4) {
        s = (l - f + 1) * 2;
        b = a;
        a = (long long *) malloc(s * sizeof(long long));
        for (int i = f; i < l; i++) {
            a[i - f] = b[i];
        }
        free(b);

        l = l - f;
        f = 0;
    }
}
