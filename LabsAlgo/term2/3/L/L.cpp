#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

const unsigned int n = (1 << 24);
const unsigned int M = (1 << 17);

unsigned int a, b, q;
unsigned int cur = 0;

unsigned int nextRand17() {
    cur = cur * a + b;
    return cur >> 15;
}

unsigned int nextRand24() {
    cur = cur * a + b;
    return cur >> 8;
}

struct node {
    node *l, *r;
    unsigned int sum;
};

vector<unsigned int> arr;

node *z = (node *) malloc(17 * n * sizeof(node) + 100);
unsigned int cnt = 0;

node *make(node *a, unsigned int val) {
    a->sum = val;
    return a;
}

node *make(node *a, node *l, node *r) {
    a->l = l;
    a->r = r;
    a->sum = 0;
    if (l) {
        a->sum += l->sum;
    }
    if (r) {
        a->sum += r->sum;
    }
    return a;
}

node *build(unsigned int l, unsigned int r) {
    if (l == r) {
        return make(&z[++cnt], 0, 0);
    }

    unsigned int m = (r + l) / 2;
    return make(&z[++cnt], build(l, m), build(m + 1, r));
}

unsigned int query(node *v, unsigned int l, unsigned int r, unsigned int lz, unsigned int rz) {
    if (lz > rz) {
        return 0;
    }

    if (l == lz && r == rz) {
        return v->sum;
    }

    unsigned int m = (l + r) / 2;
    return query(v->l, l, m, lz, min(rz, m))
           + query(v->r, m + 1, r, max(lz, m + 1), rz);
}

node *update(node *v, unsigned int l, unsigned int r, unsigned int pos) {
    if (l == r) {
        return make(&z[++cnt], v->sum + 1);
    }

    unsigned int m = (l + r) / 2;
    if (pos <= m) {
        return make(&z[++cnt], update(v->l, l, m, pos), v->r);
    } else {
        return make(&z[++cnt], v->l, update(v->r, m + 1, r, pos));
    }
}


int main() {

    freopen("find2d.in", "r", stdin);
    freopen("find2d.out", "w", stdout);

    scanf("%u %u %u", &q, &a, &b);
    node *rr = build(0, M - 1);

    vector<node *> root(n + 1, rr);

    vector<pair<unsigned int, unsigned int>> f(M);
    for (unsigned int i = 0; i < M; ++i) {
        f[i].first = nextRand24();
        f[i].second = i;
    }

    sort(f.begin(), f.end());

    size_t j = 0;
    for (size_t i = 0; i < n; ++i) {
        root[i + 1] = root[i];
        while (j < M && i == f[j].first) {
            root[i + 1] = update(root[i + 1], 0, M - 1, f[j].second);
            ++j;
        }
    }

    unsigned int ans = 0;
    unsigned int l, r, x, y, c;
    for (int i = 0; i < q; i++) {
        l = nextRand17();
        r = nextRand17();
        if (l > r) {
            swap(l, r);
        }

        x = nextRand24();
        y = nextRand24();
        if (x > y) {
            swap(x, y);
        }

        c = query(root[y + 1], 0, M - 1, l, r) - query(root[x], 0, M - 1, l, r);
        b += c;
        ans += c;
    }

    printf("%u", ans);

    free(z);
    return 0;
}