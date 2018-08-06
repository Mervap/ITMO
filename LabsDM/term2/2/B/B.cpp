#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
#include <unordered_set>

using namespace std;
const int M = 26;

int n, m, k;

vector<bool> T;
vector<vector<vector<int>>> edges;

int main() {

    freopen("problem2.in", "r", stdin);
    freopen("problem2.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);


    string s;
    cin >> s;
    cin >> n >> m >> k;
    ++n;

    T.assign(n, 0);
    edges.assign(n, vector<vector<int>>(M, vector<int>()));

    int a, b;
    char c;
    for (int i = 0; i < k; ++i) {
        cin >> a;
        T[a] = true;
    }

    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        edges[a][c - 'a'].push_back(b);
    }

    vector<unordered_set<int>> d(2);
    d[0].insert(1);

    for (int i = 0; i < s.length(); ++i) {
        int cc = s[i] - 'a';
        int t = i % 2;

        d[(1 + t) % 2].clear();
        for (int j : d[t]) {
            for (int z : edges[j][cc]) {
                d[(1 + t) % 2].insert(z);
            }
        }
    }

    bool f;
    int t = s.length() % 2;
    for (int ans : d[t]) {
        f |= T[ans];
    }

    cout << (f ? "Accepts" : "Rejects");
    return 0;

}