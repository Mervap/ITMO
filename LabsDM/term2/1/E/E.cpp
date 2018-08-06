#include <iostream>
#include <vector>

#define double long double

using namespace std;
int n;

vector<vector<double> > mul(vector<vector<double> > &a, vector<vector<double> > &b) {
    vector<vector<double> > res;
    res.assign(n, vector<double>(n));

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            for (int k = 0; k < n; ++k) {
                res[i][j] += a[i][k] * b[k][j];
            }
        }
    }
    return res;
}

vector<vector<double> > binMul(vector<vector<double> > &a, int deg) {

    vector<vector<double> > res;
    res.assign(n, vector<double>(n));

    if (deg == 1) {
        return a;
    }

    if (deg % 2 == 1) {
        res = binMul(a, deg - 1);
        return mul(a, res);
    } else {
        res = binMul(a, deg / 2);
        return mul(res, res);
    }

}

int main() {
    freopen("markchain.in", "r", stdin);
    freopen("markchain.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    cin >> n;

    vector<vector<double> > p;
    p.assign(n, vector<double>(n));


    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cin >> p[i][j];
        }
    }

    p = binMul(p, 1000000000);

    double sum = 0;
    for (int i = 0; i < n; ++i) {
        sum += p[0][i];
    }

    for (int i = 0; i < n; ++i) {
        cout << p[0][i] / sum << "\n";
    }

    return 0;
}
