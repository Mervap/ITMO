#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
#include <unordered_set>
#include <unordered_map>

using namespace std;
const int M = 26;
const int MD = 1000000007;

int n, m, k, l;

vector<bool> T;
vector<vector<int>> sum;
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
        for(int to : edges[v]) {
            sum[v][len] += ans(to, len - 1);
            sum[v][len] %= MD;
        }
        return sum[v][len];
    }
}

int main() {

    freopen("problem4.in", "r", stdin);
    freopen("problem4.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    cin >> n >> m >> k >> l;
    ++n;

    T.assign(n, 0);
    sum.assign(n, vector<int>(l + 1, -1));
    edges.assign(n, vector<int>());

    int a, b;
    char c;
    for (int i = 0; i < k; ++i) {
        cin >> a;
        T[a] = true;
    }

    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        edges[a].push_back(b);
    }

    cout << ans(1, l);

    return 0;
}