#include <iostream>
#include <vector>
#include <fstream>


using namespace std;

bool check(long long t);

vector<pair<long, long>> cass;
long long n, m, p;

void msort(vector<long long> &a, int l, int r);

void msort(vector<long long> &a, int l, int r) {
    if (l >= r) {
        return;
    }
    int mid = (r + l) / 2;
    msort(a, l, mid);
    msort(a, mid + 1, r);
    vector<long long> b(r - l + 1);

    int j1, j2;
    j1 = l;
    j2 = mid + 1;
    for (int i = l; i <= r; i++) {
        if (j1 <= mid && (j2 > r || (a[j1] >= a[j2]))) {
            b[i - l] = a[j1];
            j1++;
        } else {
            b[i - l] = a[j2];
            j2++;
        }
    }

    for (int i = l; i <= r; i++) {
        a[i] = b[i - l];
    }
}

bool check(long long t) {
    vector<long long> a;
    for (int i = 0; i < m; i++) {
        if (cass[i].first <= t && cass[i].second == 0) {
            return true;
        }
        if (cass[i].first > t) {
            continue;
        }
        a.push_back((t - cass[i].first) / cass[i].second);
    }
    msort(a, 0, a.size() - 1);

    long long k = 0;
    long long l = a.size();
    for (int i = 0; i < min(n, l); i++) {
        k += a[i];
        if (k >= p) {
            return true;
        }
    }

    return false;
}

int main() {
    freopen("supermarket.in", "r", stdin);
    freopen("supermarket.out", "w", stdout);

    scanf("%d", &m);


    long long a, b, t;
    for (int i = 0; i < m; i++) {
        scanf("%d%d%d", &a, &b, &t);
        cass.push_back(make_pair(b + t, a));
    }

    scanf("%d%d", &n, &p);

    if (p == 0) {
        cout << 0;
        return 0;
    }
    long long mid, l = -1, r = 100000000000;
    while (r - l > 1) {
        mid = (r + l) / 2;
        if (check(mid)) {
            r = mid;
        } else {
            l = mid;
        }
    }

    cout << r;
    return 0;
}
