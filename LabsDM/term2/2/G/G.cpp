#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
#include <unordered_set>
#include <unordered_map>

using namespace std;

const int N = 2002;
const int M = 26;

int n, m, k, n1, m1, k1;

vector<bool> TA(N);
vector<bool> used(N);
vector<vector<int>> edgesA(N, vector<int>(M, 0));
vector<vector<vector<int>>> edgesRev(N, vector<vector<int>>(M, vector<int>(0)));

int color[N];

void findClasses() {
    unordered_set<int> F;
    unordered_set<int> NF;
    unordered_set<int> nll;
    vector<unordered_set<int>> P;

    for (int i = 0; i < n1; ++i) {
        if (TA[i]) {
            F.insert(i);
            color[i] = 0;
        } else {
            NF.insert(i);
            color[i] = 1;
        }
    }

    P.push_back(F);
    P.push_back(NF);

    queue<pair<int, int>> queue;
    for (int i = 0; i < M; ++i) {
        queue.push({0, i});
        queue.push({1, i});
    }

    while (!queue.empty()) {
        auto cur = queue.front();
        queue.pop();
        auto curC = P[cur.first];
        unordered_map<int, vector<int>> from;
        for (auto c : curC) {
            for (auto v : edgesRev[c][cur.second]) {
                auto i = color[v];
                from[i].push_back(v);
            }
        }

        for (auto c : from) {
            if (!c.second.empty()) {
                auto cur2 = c.first;
                if (from[cur2].size() < P[cur2].size()) {
                    auto j = (int) P.size();
                    P.push_back(nll);

                    for (auto v : from[cur2]) {
                        P[cur2].erase(v);
                        P[j].insert(v);
                    }

                    if (P[cur2].size() < P[j].size()) {
                        swap(P[cur2], P[j]);
                    }
                    for (int v : P[j]) {
                        color[v] = j;
                    }
                    for (int z = 0; z < M; ++z) {
                        queue.push({j, z});
                    }
                }
            }
        }
    }
}

int main() {

    freopen("equivalence.in", "r", stdin);
    freopen("equivalence.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    cin >> n >> m >> k;
    ++n;

    int a;
    for (int i = 0; i < k; ++i) {
        cin >> a;
        TA[a] = true;
    }

    int b;
    char c;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        edgesA[a][c - 'a'] = b;
    }
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < M; j++) {
            edgesRev[edgesA[i][j]][j].push_back(i);
        }
    }

    cin >> n1 >> m1 >> k1;
    --n;
    ++n1;
    n1 += n;
    m1 += m;
    k1 += k;

    for (int i = k; i < k1; ++i) {
        cin >> a;
        TA[a + n] = true;
    }

    for (int i = m; i < m1; ++i) {
        cin >> a >> b >> c;
        edgesA[a + n][c - 'a'] = b + n;
    }
    for (int i = n + 1; i < n1; i++) {
        for (int j = 0; j < M; j++) {
            edgesRev[edgesA[i][j]][j].push_back(i);
        }
    }

    findClasses();

    cout << ((color[1] == color[n + 1]) ? "YES" : "NO");

    return 0;

}