#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <map>
#include <algorithm>
#include <cmath>

using namespace std;

const double INF = 1e7;
const double eps = 1e-15;

struct node {
    int x, y;

    double dist(node const &other) {
        int dx = abs(x - other.x);
        int dy = abs(y - other.y);
        return sqrt(dx * dx + dy * dy);
    }
};

vector<node> v;
vector<double> d;
vector<bool> used;

int main() {
    int n;

    cin >> n;
    int x, y;
    for (int i = 0; i < n; ++i) {
        cin >> x >> y;
        v.push_back({x, y});
    }

    d.assign(n, INF);
    used.resize(n);
    d[0] = 0;
    double ans = 0;
    for (int i = 0; i < n; ++i) {
        int cur = -1;
        for (int j = 0; j < n; ++j) {
            if (!used[j] && (cur == -1 || d[cur] - d[j] > eps)) {
                cur = j;
            }
        }

        ans += d[cur];
        used[cur] = true;
        for (int j = 0; j < n; ++j) {
            if (!used[j] && d[j] - v[j].dist(v[cur]) > eps) {
                d[j] = v[j].dist(v[cur]);
            }
        }
    }


    cout.precision(10);
    cout << ans;
    return 0;
}