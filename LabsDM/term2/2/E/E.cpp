#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
#include <set>
#include <map>

using namespace std;
const int M = 26;
const int MD = 1000000007;

int n, m, k, l;

vector<bool> T;
vector<bool> T_n;
vector<vector<int>> sum;
vector<vector<pair<int, int>>> edges_n;
vector<vector<int>> edges;

int ans(int v, int len) {
    if (sum[v][len] != -1) {
        return sum[v][len];
    }

    if (len == 0) {
        sum[v][len] = T[v];
        return sum[v][len];
    } else {
        sum[v][len] = 0;
        for (int to : edges[v]) {
            sum[v][len] += ans(to, len - 1);
            sum[v][len] %= MD;
        }
        return sum[v][len];
    }
}

void tompson() {
    set<set<int>> v;
    map<set<int>, int> num;
    queue<set<int>> q;

    sum.assign(101, vector<int>(l + 1, -1));
    edges.assign(101, vector<int>());
    T.assign(101, 0);

    q.push({1});
    v.insert({1});
    int cnt = 1;
    num[{1}] = cnt;
    T[1] = T_n[1];

    while (!q.empty()) {
        auto tec = q.front();
        q.pop();

        for (int i = 0; i < M; ++i) {
            set<int> nw;
            for (int j : tec) {
                for (auto z : edges_n[j]) {
                    if (z.second == i) {
                        nw.insert(z.first);
                    }
                }
            }

            if (nw.empty()) {
                continue;
            }

            if (v.count(nw) == 0) {
                v.insert(nw);
                num[nw] = ++cnt;
                q.push(nw);

                for (int j : nw) {
                    if (T_n[j]) {
                        T[num[nw]] = true;
                        break;
                    }
                }
            }
            edges[num[tec]].push_back(num[nw]);
        }

    }

}

int main() {

    freopen("problem5.in", "r", stdin);
    freopen("problem5.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    cin >> n >> m >> k >> l;
    ++n;

    T_n.assign(n, 0);
    edges_n.assign(n, vector<pair<int, int>>());

    int a, b;
    char c;
    for (int i = 0; i < k; ++i) {
        cin >> a;
        T_n[a] = true;
    }

    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        edges_n[a].push_back({b, c - 'a'});
    }

    tompson();

    cout << ans(1, l);

    return 0;
}