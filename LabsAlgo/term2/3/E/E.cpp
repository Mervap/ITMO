#include <iostream>
#include <vector>

using namespace std;

vector<long long> tree;
vector<long long> arr;

void build(int i, int l, int r) {
    if (l == r) {
        tree[i] = arr[l];
        return;
    }

    int m = (r + l) / 2;
    build(2 * i, l, m);
    build(2 * i + 1, m + 1, r);
    tree[i] = tree[2 * i] + tree[2 * i + 1];
}

long long query(int i, int l, int r, int lz, int rz) {
    if (lz > rz) {
        return 0;
    }

    if (l == lz && r == rz) {
        return tree[i];
    }

    int m = (l + r) / 2;
    long long ll = query(2 * i, l, m, lz, min(m, rz));
    long long rr = query(2 * i + 1, m + 1, r, max(m + 1, lz), rz);
    return ll + rr;

}

void update(int i, int l, int r, int j, long long v) {
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
    tree[i] = tree[2 * i] + tree[2 * i + 1];
}

int main() {

    freopen("rsq.in", "r", stdin);
    freopen("rsq.out", "w", stdout);

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
        if (s == "sum") {
            int l, r;
            cin >> l >> r;
            cout << query(1, 0, n - 1, l - 1, r - 1) << "\n";
        } else {
            int i;
            long long v;
            cin >> i >> v;
            update(1, 0, n - 1, i - 1, v);
        }
    }
    return 0;
}