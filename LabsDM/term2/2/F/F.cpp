#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
#include <unordered_set>
#include <unordered_map>

using namespace std;
const int M = 26;

int n, m, k, n1, m1, k1;

vector<bool> TA;
vector<bool> TB;
vector<bool> used;
vector<vector<int>> edgesA;
vector<vector<int>> edgesB;

bool dfs(int a, int b) {
    if (used[a]) {
        return true;
    }

    used[a] = true;

    if (TA[a] ^ TB[b] == 1) {
        return false;
    }

    for (int i = 0; i < M; ++i) {
        int toA = edgesA[a][i];
        int toB = edgesB[b][i];

        if (toA == 0 ^ toB == 0) {
            return false;
        }

        if (!used[toA]) {
            if (!dfs(toA, toB)) {
                return false;
            }
        }
    }
    return true;
}

int main() {

    freopen("isomorphism.in", "r", stdin);
    freopen("isomorphism.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    cin >> n >> m >> k;
    ++n;

    TA.assign(n, 0);
    used.assign(n, 0);
    edgesA.assign(n, vector<int>(M, 0));

    int a, b;
    char c;
    for (int i = 0; i < k; ++i) {
        cin >> a;
        TA[a] = true;
    }

    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        edgesA[a][c - 'a'] = b;
    }

    cin >> n1 >> m1 >> k1;
    ++n1;

    if (n != n1) {
        cout << "NO";
        return 0;
    }

    TB.assign(n1, 0);
    edgesB.assign(n1, vector<int>(M, 0));

    for (int i = 0; i < k1; ++i) {
        cin >> a;
        TB[a] = true;
    }

    for (int i = 0; i < m1; ++i) {
        cin >> a >> b >> c;
        edgesB[a][c - 'a'] = b;
    }

    cout << (dfs(1, 1) ? "YES" : "NO");

    return 0;

}