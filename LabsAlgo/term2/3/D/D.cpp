#include <iostream>
#include <vector>

using namespace std;

vector<int> tree;
vector<int> arr;

void build(int i, int l, int r) {
    if (l == r) {
        tree[i] = arr[l];
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

void update(int i, int l, int r, int j, int v) {
    if (l == r) {
        tree[i] = v;
        return;
    }

    int m = (r + l) / 2;
    if (l <= j && j <= m) {
        update(2 * i, l, m, j, v);
    } else {
        update(2 * i + 1, m + 1, r, j, v);
    }
    tree[i] = min(tree[2 * i], tree[2 * i + 1]);
}

int main() {

    freopen("rmq.in", "r", stdin);
    freopen("rmq.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    int n;
    cin >> n;

    arr.resize(n);
    tree.resize(4 * n);
    for (size_t i = 0; i < n; ++i) {
        cin >> arr[i];
    }

    build(1, 0, n - 1);

    string s;
    while (cin >> s) {
        if (s == "min") {
            int l, r;
            cin >> l >> r;
            cout << query(1, 0, n - 1, l - 1, r - 1) << "\n";
        } else {
            int i, v;
            cin >> i >> v;
            update(1, 0, n - 1, i - 1, v);
        }
    }
    return 0;
}