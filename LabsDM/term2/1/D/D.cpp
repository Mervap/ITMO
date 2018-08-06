#include <iostream>
#include <vector>
#include <math.h>

using namespace std;
const double eps = 1e-9;
int n, cnt;

vector<vector<double>> mul(vector<vector<double>> &a, vector<vector<double>> &b) {
    vector<vector<double>> res;
    res.assign(n - cnt, vector<double>(cnt, 0));

    for (int i = 0; i < n - cnt; i++) {
        for (int j = 0; j < cnt; j++) {
            for (int k = 0; k < n - cnt; k++) {
                res[i][j] += a[i][k] * b[k][j];
            }
        }
    }
    return res;
}

int main() {
    freopen("absmarkchain.in", "r", stdin);
    freopen("absmarkchain.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    int m;

    cin >> n >> m;

    vector<vector<double> > p(n, vector<double>(n));
    vector<vector<double> > q(n, vector<double>(n));
    vector<vector<double> > r(n, vector<double>(n));
    vector<vector<double> > e(n, vector<double>(n));
    vector<vector<double> > f(n, vector<double>(n));

    vector<bool> imp(n);
    int a, b;
    cnt = 0;

    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        cin >> p[a - 1][b - 1];

        if (a == b && abs(1 - p[a - 1][b - 1]) < eps) {
            imp[a - 1] = true;
            ++cnt;
        }
    }

    int cnt_imp = 0, cnt_unimp = 0;
    vector<int> pos(n);
    for (int i = 0; i < n; ++i) {
        if (imp[i]) {
            pos[i] = cnt_imp;
            ++cnt_imp;
        } else {
            pos[i] = cnt_unimp;
            ++cnt_unimp;
        }
    }

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            if (!imp[i] && imp[j]) {
                r[pos[i]][pos[j]] = p[i][j];
            } else if (!imp[i] && !imp[j]) {
                q[pos[i]][pos[j]] = p[i][j];
            }
        }
    }

    for (int i = 0; i < n - cnt; ++i) {
        e[i][i] = f[i][i] = 1;
    }

    for (int i = 0; i < n - cnt; ++i) {
        for (int j = 0; j < n - cnt; ++j) {
            e[i][j] -= q[i][j];
        }
    }

    for (int i = 0; i < n - cnt; ++i) {
        if (abs(1 - e[i][i]) > eps) {
            double z = e[i][i];
            for (int j = 0; j < n - cnt; ++j) {
                e[i][j] /= z;
                f[i][j] /= z;
            }
        }

        for (int j = 0; j < n - cnt; ++j) {
            if (i == j) {
                continue;
            }

            double z = e[j][i];
            for (int k = 0; k < n - cnt; ++k) {
                e[j][k] -= z * e[i][k];
                f[j][k] -= z * f[i][k];
            }
        }
    }

    p = mul(f, r);

    double ans;
    for (int i = 0; i < n; i++) {
        ans = imp[i];
        if (ans) {
            for (int j = 0; j < n - cnt; ++j) {
                ans += p[j][pos[i]];
            }
            ans /= n;
        }

        printf("%.9f\n", ans);
    }
    return 0;
}
