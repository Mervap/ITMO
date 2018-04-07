#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

unsigned int cur = 0, a1, b1, c1, d;

unsigned int nextRand24();

unsigned int nextRand32();

unsigned int nextRand24() {
    cur = cur * a1 + b1;
    return cur >> 8;
}

unsigned int nextRand32() {
    c1 = nextRand24();
    d = nextRand24();
    return (c1 << 8) ^ d;
}


void countSort(unsigned int *p, unsigned int *&ans, int l, int k) {

    k *= 8;
    int mask = 255 << k;
    unsigned int *c = new unsigned int[257];
    for (int i = 0; i < 257; i++) {
        c[i] = 0;
    }

    c++;
    for (int i = 0; i < l; i++) {
        c[(p[i] & mask) >> k]++;
    }
    for (int i = 0; i < 255; i++) {
        c[i + 1] += c[i];
    }

    c--;
    for (int i = 0; i < l; i++) {
        ans[c[(p[i] & mask) >> k]] = p[i];
        c[(p[i] & mask) >> k]++;
    }
}

void radixSort(unsigned int *p, int l) {

    unsigned int *t = new unsigned int[l];

    countSort(p, t, l, 0);
    countSort(t, p, l, 1);
    countSort(p, t, l, 2);
    countSort(t, p, l, 3);
}

int main() {

    freopen("buckets.in", "r", stdin);
    freopen("buckets.out", "w", stdout);


    int t, n;
    scanf("%d%d%d%d", &t, &n, &a1, &b1);
    unsigned int *a = new unsigned int[n];

    for (int tt = 0; tt < t; tt++) {
        unsigned int mx = 0;
        unsigned int l = 0;
        for (int i = 0; i < n; i++) {
            l = nextRand32();
            if (l > mx) {
                mx = l;
            }
            a[i] = l;
        }


        radixSort(a, n);

        unsigned long long ans = 0;
        for (int i = 1; i <= n; i++) {
            ans += (unsigned long long) a[i - 1] * (unsigned long long) i;
        }

        cout << ans << endl;
    }


    return 0;

}
