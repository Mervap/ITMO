#include <iostream>
#include <vector>

using namespace std;

int n, m;
vector<int> tree;

void build(int i, int l, int r) {
    if (l == r) {
        tree[i] = l;
        return;
    }

    int m = (r + l) / 2;
    build(2 * i, l, m);
    build(2 * i + 1, m + 1, r);
    tree[i] = min(tree[2 * i], tree[2 * i + 1]);
}

int query(int i, int l, int r, int lz, int rz) {
    if (lz > rz) {
        return INT_MAX;
    }

    if (l == lz && r == rz) {
        return tree[i];
    }

    int m = (l + r) / 2;
    int ll = query(2 * i, l, m, lz, min(m, rz));
    int rr = query(2 * i + 1, m + 1, r, max(m + 1, lz), rz);
    return min(ll, rr);
}

void update(int i, int l, int r, int p, int v) {
    if (l == r) {
        tree[i] = v;
        return;
    }

    int m = (r + l) / 2;
    if (p > m) {
        update(2 * i + 1, m + 1, r, p, v);
    } else {
        update(2 * i, l, m, p, v);
    }
    tree[i] = min(tree[2 * i], tree[2 * i + 1]);
}


int main() {

    freopen("parking.in", "r", stdin);
    freopen("parking.out", "w", stdout);

    scanf("%d %d", &n, &m);
    tree.resize(4 * n);

    build(1, 1, n);
    string s;
    while (cin >> s) {
        int i;
        scanf("%d", &i);

        if (s == "enter") {
            int ans = query(1, 1, n, i, n);
            if (ans == INT_MAX) {
                ans = query(1, 1, n, 1, i);
            }
            update(1, 1, n, ans, INT_MAX);
            printf("%d\n", ans);
        } else {
            update(1, 1, n, i, i);
        }
    }
}
