#include <iostream>
#include <set>
#include <vector>
#include <cstring>

using namespace std;

int MD, m, n;

struct matrix {
    int m[2][2];

    void read() {
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                scanf("%d", &m[i][j]);
            }
        }
    }

    void print() {
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                printf("%d ", m[i][j]);
            }
            putchar('\n');
        }
    }
};

matrix operator*(matrix &a, matrix &b) {
    matrix c;
    memset(c.m, 0, sizeof(c.m));

    for (int i = 0; i < 2; ++i) {
        for (int j = 0; j < 2; ++j) {
            for (int k = 0; k < 2; ++k) {
                c.m[i][j] += a.m[i][k] * b.m[k][j];
                c.m[i][j] %= MD;
            }
        }
    }

    return c;
}

vector<matrix> tree;
vector<matrix> a;

void build(int i, int l, int r) {
    if (l == r) {
        tree[i] = a[l];
        return;
    }

    int m = (l + r) / 2;
    build(2 * i, l, m);
    build(2 * i + 1, m + 1, r);
    tree[i] = tree[2 * i] * tree[2 * i + 1];
}

matrix query(int i, int l, int r, int lz, int rz) {

    if (l == lz && r == rz) {
        return tree[i];
    }

    int m = (l + r) / 2;

    if (rz < m + 1) {
        return query(2 * i, l, m, lz, rz);
    } else if (lz > m) {
        return query(2 * i + 1, m + 1, r, lz, rz);
    } else {
        matrix A = query(2 * i, l, m, lz, m);
        matrix B = query(2 * i + 1, m + 1, r, m + 1, rz);
        return A * B;
    }
}

int main() {

    freopen("crypto.in", "r", stdin);
    freopen("crypto.out", "w", stdout);

    scanf("%d %d %d", &MD, &n, &m);

    tree.resize(4 * n);
    a.resize(n);

    for (size_t i = 0; i < n; ++i) {
        a[i].read();
    }

    build(1, 0, n - 1);

    for (int i = 0; i < m; ++i) {
        int l, r;
        scanf("%d %d", &l, &r);
        query(1, 0, n - 1, l - 1, r - 1).print();
        putchar('\n');
    }
}