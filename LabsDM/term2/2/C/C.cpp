#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
#include <unordered_set>
#include <unordered_map>

using namespace std;
const int M = 26;
const int MD = 1000000007;

int n, m, k;

vector<bool> T;
vector<bool> used;
vector<bool> helpful;
vector<int> sum;
vector<vector<int>> edges;
vector<vector<vector<int>>> edgesRev;

vector<int> topsort;

void help(int v) {
    helpful[v] = true;
    used[v] = true;
    for (int i = 0; i < M; ++i) {
        for (int to : edgesRev[v][i]) {
            if (!used[to]) {
                help(to);
            }
        }
    }
}

bool dfs(int v) {
    for (int i = 0; i < M; ++i) {
        int to = edges[v][i];
        if (helpful[to]) {
            if (!used[to]) {
                used[to] = true;
                if (dfs(to)) {
                    return true;
                }
                used[to] = false;
            } else {
                return true;
            }
        }
    }
    return false;
}

void ans(int v) {
    used[v] = true;
    for (int i = 0; i < M; ++i) {
        for (int to : edgesRev[v][i]) {
            if (!used[to]) {
                ans(to);
            }
        }
    }

    topsort.push_back(v);
}


int main() {

    freopen("problem3.in", "r", stdin);
    freopen("problem3.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);

    cin >> n >> m >> k;
    ++n;

    T.assign(n, 0);
    used.assign(n, 0);
    helpful.assign(n, 0);
    sum.assign(n, 0);
    edges.assign(n, vector<int>(M, 0));
    edgesRev.assign(n, vector<vector<int>>(M, vector<int>(0)));

    int a, b;
    char c;
    for (int i = 0; i < k; ++i) {
        cin >> a;
        T[a] = true;
        helpful[a] = true;
    }

    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        edges[a][c - 'a'] = b;
    }

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < M; j++) {
            edgesRev[edges[i][j]][j].push_back(i);
        }
    }

    for (int i = 1; i < n; ++i) {
        if (T[i]) {
            help(i);
        }
    }

    used.assign(n, 0);
    used[1] = true;
    if (dfs(1)) {
        cout << -1;
        return 0;
    }
    used[1] = false;
    sum[1] = 1;

    for (int i = 1; i < n; ++i) {
        if (T[i] && !used[i]) {
            ans(i);
        }
    }

    sum[1] = 1;
    for (int i = 0; i < topsort.size(); ++i) {
        int t = topsort[i];
        for (int i = 0; i < M; ++i) {
            for (int to : edgesRev[t][i]) {
                sum[t] += sum[to];
                sum[t] %= MD;
            }
        }
    }

    int answer = 0;
    for (int i = 1; i < n; ++i) {
        if (T[i]) {
            answer += sum[i];
            answer %= MD;
        }
    }

    cout << answer;

    return 0;

}