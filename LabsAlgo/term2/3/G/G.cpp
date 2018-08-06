#include <iostream>
#include <vector>

using namespace std;

vector<long long> tree;
vector<long long> add;
vector<long long> set;
vector<bool> reset;

void build(int i, int l, int r) {
    if (l == r) {
        tree[i] = add[l];
        return;
    }

    int m = (r + l) / 2;
    build(2 * i, l, m);
    build(2 * i + 1, m + 1, r);
    tree[i] = min(tree[2 * i], tree[2 * i + 1]);
}

void update_v(int i, int l, int r) {
    if (reset[i]) {
        tree[2 * i] = set[i];
        tree[2 * i + 1] = set[i];
        if (l != r) {
            set[2 * i] = set[i];
            reset[2 * i] = true;

            set[2 * i + 1] = set[i];
            reset[2 * i + 1] = true;

            add[2 * i] = 0;
            add[2 * i + 1] = 0;
        }
        reset[i] = false;
    }

    if (l != r) {
        tree[2 * i] += add[i];
        tree[2 * i + 1] += add[i];
        add[2 * i] += add[i];
        add[2 * i + 1] += add[i];
    }
    add[i] = 0;
}

long long query(int i, int l, int r, int lz, int rz) {
    if (lz > rz) {
        return LLONG_MAX;
    }

    if (l == lz && r == rz) {
        return tree[i];
    }

    update_v(i, l, r);

    int m = (l + r) / 2;
    long long ll = query(2 * i, l, m, lz, min(m, rz));
    long long rr = query(2 * i + 1, m + 1, r, max(m + 1, lz), rz);
    return min(ll, rr);

}

void update(int i, int l, int r, int lz, int rz, long long v, int type) {
    if (lz > rz) {
        return;
    }

    if (l == lz && r == rz) {
        if(type) {
            reset[i] = true;
            set[i] = v;
            tree[i] = v;
            add[i] = 0;
        } else {
            add[i] += v;
            tree[i] += v;
        }
        return;
    }

    update_v(i, l, r);

    int m = (l + r) / 2;
    update(2 * i, l, m, lz, min(m, rz), v, type);
    update(2 * i + 1, m + 1, r, max(m + 1, lz), rz, v, type);
    tree[i] = min(tree[2 * i], tree[2 * i + 1]);
}

int main() {

    freopen("rmq2.in", "r", stdin);
    freopen("rmq2.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    int n;
    scanf("%d", &n);

    add.resize(n);
    tree.resize(4 * n);
    for (size_t i = 0; i < n; ++i) {
        scanf("%lld", &add[i]);
    }

    build(1, 0, n - 1);
    add.assign(4 * n, 0);
    set.assign(4 * n, 0);
    reset.assign(4 * n, 0);

    string s;
    while (cin >> s) {
        int l, r;
        long long v;
        if (s == "min") {
            scanf("%d %d", &l, &r);
            printf("%lld \n", query(1, 0, n - 1, l - 1, r - 1));
        } else if (s == "add") {
            scanf("%d %d %lld", &l, &r, &v);
            update(1, 0, n - 1, l - 1, r - 1, v, 0);
        } else {
            scanf("%d %d %lld", &l, &r, &v);
            update(1, 0, n - 1, l - 1, r - 1, v, 1);
        }
    }
    return 0;
}