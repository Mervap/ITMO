#include <iostream>
#include <set>
#include <vector>

using namespace std;

const long long MD = 1000000007;

struct line {
    int x1, x2, y1, y2;
};

line operator*(const line &a, const line &b) {
    line c;
    if (a.x1 > b.x2 || a.x2 < b.x1 || a.y1 > b.y2 || a.y2 < b.y1) {
        c.x1 = c.x2 = c.y1 = c.y2 = 0;
        return c;
    }
    c.x1 = max(a.x1, b.x1);
    c.x2 = min(a.x2, b.x2);
    c.y1 = max(a.y1, b.y1);
    c.y2 = min(a.y2, b.y2);
    return c;
}

int sqr(line a) {
    long long dx = a.x2 - a.x1;
    long long dy = a.y2 - a.y1;
    return (dx * dy % MD);
}

vector<int> lg;
vector<vector<line>> tree;
line st[128][8][128][8];
size_t n, m;

int main() {

    freopen("pail.in", "r", stdin);
    freopen("pail.out", "w", stdout);

    cin >> n >> m;

    lg.resize(max(n, m) + 1);
    tree.assign(n, vector<line>(m));

    for (int i = 0; i < n; i++) {
        tree[i].resize(m);
        for (int j = 0; j < m; j++) {
            int x1, x2, y1, y2;
            cin >> x1 >> y1 >> x2 >> y2;
            tree[i][j].x1 = min(x1, x2);
            tree[i][j].y1 = min(y1, y2);
            tree[i][j].x2 = max(x1, x2);
            tree[i][j].y2 = max(y1, y2);
        }
    }

    lg[1] = 0;
    for (size_t i = 2; i < max(n, m) + 1; ++i) {
        lg[i] = lg[i / 2] + 1;
    }

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < m; ++j) {
            st[i][0][j][0] = tree[i][j];
        }
        for (int k = 1; k < lg[m] + 1; ++k) {
            for (int j = 0; j + (1 << k) - 1 < m; ++j) {
                st[i][0][j][k] = st[i][0][j][k - 1] * st[i][0][j + (1 << (k - 1))][k - 1];
            }
        }
    }

    for (int k1 = 1; k1 < lg[n] + 1; ++k1) {
        for (int i = 0; i + (1 << k1) - 1 < n; ++i) {
            for (int j = 0; j < m; j++) {
                st[i][k1][j][0] = st[i][k1 - 1][j][0] * st[i + (1 << (k1 - 1))][k1 - 1][j][0];
            }
            for (int k2 = 1; k2 < lg[m] + 1; ++k2) {
                for (int j = 0; j + (1 << k2) - 1 < m; ++j) {
                    st[i][k1][j][k2] = st[i][k1][j][k2 - 1] * st[i][k1][j + (1 << (k2 - 1))][k2 - 1];
                }
            }
        }
    }

    long long q, a, b;
    vector<long long> v(5);
    cin >> q >> a >> b >> v[4];

    int x1, x2, y1, y2;
    int ans = 0;
    for (size_t i = 0; i < q; ++i) {
        v[0] = v[4];
        for (int j = 1; j < 5; ++j) {
            v[j] = (a * v[j - 1] + b) % MD;
        }

        x1 = min(v[1] % n, v[3] % n);
        x2 = max(v[1] % n, v[3] % n) + 1;
        y1 = min(v[2] % m, v[4] % m);
        y2 = max(v[2] % m, v[4] % m) + 1;

        int k1 = lg[x2 - x1];
        int k2 = lg[y2 - y1];

        ans += sqr((st[x1][k1][y1][k2] * st[x2 - (1 << k1)][k1][y1][k2] * st[x1][k1][y2 - (1 << k2)][k2] *
                    st[x2 - (1 << k1)][k1][y2 - (1 << k2)][k2]));
        ans %= MD;
    }

    cout << ans;
}