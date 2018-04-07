#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <algorithm>

#define f first
#define s second

using namespace std;

vector<pair<long long, int> > q;
vector<int> p;

bool comp(pair<pair<int, int>, pair<int, int> > a, pair<pair<int, int>, pair<int, int> > b) { return ((a.f.f < b.f.f) ||
                                                                                                      ((a.f.f ==
                                                                                                        b.f.f) &&
                                                                                                       (a.f.s <
                                                                                                        b.f.s)));
}

void sift_up(int i) {
    if (i == 0) {
        return;
    }

    if (q[i].f > q[(i - 1) / 2].f) {
        pair<long long, int> tmp = q[i];
        q[i] = q[(i - 1) / 2];
        q[(i - 1) / 2] = tmp;

        int tmp1 = p[q[i].s];
        p[q[i].s] = p[q[(i - 1) / 2].s];
        p[q[(i - 1) / 2].s] = tmp1;
        sift_up((i - 1) / 2);
    }
}

void sift_down(int i) {
    if (i * 2 + 1 > (int) q.size() - 1 && i * 2 + 2 > (int) q.size() - 1) {
        return;
    }

    int j = i * 2 + 1;
    if (i * 2 + 2 < (int) q.size() && q[i * 2 + 2].f > q[i * 2 + 1].f) {
        j = i * 2 + 2;
    }
    if (q[i].f < q[j].f) {
        pair<long long, int> tmp = q[i];
        q[i] = q[j];
        q[j] = tmp;

        int tmp1 = p[q[i].s];
        p[q[i].s] = p[q[j].s];
        p[q[j].s] = tmp1;
        sift_down(j);
    }
}

int main() {
    freopen("rmq.in", "r", stdin);
    freopen("rmq.out", "w", stdout);

    int l, r, z;

    int n, m;
    scanf("%d%d", &n, &m);

    vector<pair<pair<int, int>, pair<int, int> > > a;

    for (int i = 0; i < m; ++i) {
        scanf("%d%d%d", &l, &r, &z);
        a.push_back(make_pair(make_pair(l, 1), make_pair(z, i)));
        a.push_back(make_pair(make_pair(r, 2), make_pair(z, i)));
    }

    sort(a.begin(), a.end(), comp);

    vector<long long> ans(n + 1, -10000000000);
    p.assign(m + 1, -1);

    int t = 1;
    for (int i = 0; i < (int) a.size() - 1; ++i) {
        while (t != a[i].f.f) {
            if (ans[t] == -10000000000) {
                if (q.empty()) {
                    ans[t] = 0;
                } else {
                    ans[t] = max(ans[t], q[0].f);
                }
            }
            ++t;
        }
        if (a[i].f.s == 1) {
            q.push_back(make_pair(a[i].s.f, a[i].s.s));
            p[a[i].s.s] = ((int) q.size() - 1);

            sift_up((int) q.size() - 1);

            ans[a[i].f.f] = max(ans[a[i].f.f], q[0].f);
        } else {
            ans[a[i].f.f] = max(ans[a[i].f.f], q[0].f);

            q[p[a[i].s.s]].f = 10000000000;
            sift_up(p[a[i].s.s]);

            p[q[0].s] = -1;

            pair<int, int> tmp = q[0];
            q[0] = q[q.size() - 1];
            q[q.size() - 1] = tmp;
            q.erase(q.end() - 1);

            if (q.empty()) {
                continue;
            }

            p[q[0].s] = 0;

            sift_down(0);
        }
    }

    ans[0] = 0;
    for (int i = 1; i <= n; ++i) {
        if (ans[i] == -10000000000) {
            ans[i] = ans[i - 1];
        }

        cout << ans[i] << " ";
    }
    return 0;
}
