#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
#include <unordered_set>
#include <unordered_map>

using namespace std;

const int N = 50001;
const int M = 26;

int n, m, k;

vector<bool> TA(N);
vector<bool> used(N);
vector<vector<int>> edgesA(N, vector<int>(M, 0));
vector<vector<vector<int>>> edgesRev(N, vector<vector<int>>(M, vector<int>(0)));

vector<int> numerate(N);
vector<vector<int>> newEdges(N, vector<int>(M, -1));
vector<pair<pair<int, int>, int>> newGraphEdges;


void dfs(int v) {
    if (v == 0) {
        return;
    }

    used[v] = true;
    for (int i = 0; i < M; ++i) {
        if (!used[edgesA[v][i]]) {
            dfs(edgesA[v][i]);
        }
    }
}

int color[N];

void findClasses() {
    unordered_set<int> F;
    unordered_set<int> NF;
    unordered_set<int> nll;
    vector<unordered_set<int>> P;

    for (int i = 0; i < n; ++i) {
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

int newN = 0;
void makeGraph(int v) {
    if (numerate[v] == 0) {
        numerate[v] = ++newN;
        for (int i = 0; i < M; ++i) {
            if (newEdges[v][i] != -1) {
                makeGraph(newEdges[v][i]);
                newGraphEdges.push_back({{numerate[v], numerate[newEdges[v][i]]}, i});
            }
        }
    }
}

int main() {

    freopen("fastminimization.in", "r", stdin);
    freopen("fastminimization.out", "w", stdout);

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

    dfs(1);
    findClasses();

    for (int i = 0; i < n; ++i) {
        if (color[i] != color[0] && used[i]) {
            for (int j = 0; j < M; ++j) {
                if (color[edgesA[i][j]] != color[0] && used[edgesA[i][j]]) {
                    newEdges[color[i]][j] = color[edgesA[i][j]];
                }
            }
        }
    }

    makeGraph(color[1]);

    unordered_set<int> newT;
    for (int i = 1; i < n; i++) {
        if (TA[i] && color[i] != color[0] && used[i]) {
            newT.insert(numerate[color[i]]);
        }
    }

    auto newK = (int) newT.size();
    auto newM = (int) newGraphEdges.size();

    cout << newN << " " << newM << " " << newK << "\n";
    for (int t : newT) {
        cout << t << " ";
    }

    cout << "\n";
    for (int i = 0; i < newM; ++i) {
        cout << newGraphEdges[i].first.first << " " << newGraphEdges[i].first.second << " "
             << ((char) (newGraphEdges[i].second + 'a')) << "\n";
    }

    return 0;

}