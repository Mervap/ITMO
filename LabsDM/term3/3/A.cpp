#include <iostream>
#include <vector>
#include <algorithm>
#include <set>


using namespace std;

struct t {
    long long d, w;

    bool operator<(t const &other) {
        return (w > other.w || (w == other.w && d < other.d));
    }
};

vector<t> ds;
set<long long> cl;

int main() {
    int n;

    freopen("schedule.in", "r", stdin);
    freopen("schedule.out", "w", stdout);

    cin >> n;

    ds.resize(n);
    for (int i = 0; i < n; ++i) {
        cin >> ds[i].d >> ds[i].w;
        cl.insert(i + 1);
    }

    sort(ds.begin(), ds.end());

    long long sum = 0;
    for (int i = 0; i < n; ++i) {
        auto it = cl.lower_bound(ds[i].d);
        if (*it == ds[i].d) {
            cl.erase(it);
            continue;
        }

        if (it == cl.begin()) {
            sum += ds[i].w;
            continue;
        }

        cl.erase(--it);
    }

    cout << sum;
    return 0;
}