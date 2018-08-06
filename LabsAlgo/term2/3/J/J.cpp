#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

const int M = 1000000;

struct vertex {
    int y, cnt = 0;
};

struct line {
    int x, y, y1, v;

    bool operator<(const line &b) const {
        return x < b.x || x == b.x && v > b.v;
    }
};

vector<vertex> tree;
vector<int> add;

vertex max(vertex &a, vertex &b) {
    if (a.cnt > b.cnt) {
        return a;
    } else {
        return b;
    }
}

void build(int i, int l, int r) {
    if (l == r) {
        tree[i] = {l, 0};
        return;
    }

    int m = (r + l) / 2;
    build(2 * i, l, m);
    build(2 * i + 1, m + 1, r);
    tree[i] = max(tree[2 * i], tree[2 * i + 1]);
}

vertex query() {
    return tree[1];
}

void update_v(int i, int l, int r) {
    if (l != r) {
        tree[2 * i].cnt += add[i];
        tree[2 * i + 1].cnt += add[i];
        add[2 * i] += add[i];
        add[2 * i + 1] += add[i];
    }
    add[i] = 0;
}

void update(int i, int l, int r, int lz, int rz, int v) {
    if (lz > rz) {
        return;
    }

    if (l == lz && r == rz) {
        add[i] += v;
        tree[i].cnt += v;
        return;
    }
    update_v(i, l, r);

    int m = (l + r) / 2;
    update(2 * i, l, m, lz, min(m, rz), v);
    update(2 * i + 1, m + 1, r, max(m + 1, lz), rz, v);
    tree[i] = max(tree[2 * i], tree[2 * i + 1]);
}

int main() {

    freopen("windows.in", "r", stdin);
    freopen("windows.out", "w", stdout);

    int n;
    scanf("%d", &n);

    tree.resize(8 * M + 1);
    add.resize(8 * M + 1);
    build(1, 1, 2 * M);

    vector<line> q;

    for (size_t i = 0; i < n; ++i) {
        int x, x1, y, y1;
        scanf("%d %d %d %d", &x, &y, &x1, &y1);
        x += M, x1 += M, y += M, y1 += M;

        q.push_back({x, y, y1, 1});
        q.push_back({x1, y, y1, -1});
    }

    sort(q.begin(), q.end());

    int ansx, ansy, ans = -1;
    for (size_t i = 0; i < q.size(); ++i) {
        update(1, 1, 2 * M, q[i].y, q[i].y1, q[i].v);
        vertex mx = query();
        if (mx.cnt > ans) {
            ansx = q[i].x;
            ansy = mx.y;
            ans = mx.cnt;
        }
    }

    printf("%d\n%d %d", ans, ansx - M, ansy - M);

    return 0;
}