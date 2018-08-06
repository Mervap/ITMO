#include <iostream>
#include <vector>
#include <queue>

using namespace std;

int center;
int range = 0;
int n;
int ans;
vector<vector<int>> gr;
vector<bool> was;

void bfs(int v, int ind) {
    vector<int> d(n, n + 1);
    vector<int> p(n);
    queue<int> q;
    q.push(v);
    d[v] = 0;

    int tec;
    while (!q.empty()) {
        tec = q.front();
        q.pop();
        was[tec] = true;

        for (int i = 0; i < gr[tec].size(); ++i) {
            int nx = gr[tec][i];
            if (!was[nx]) {
                q.push(nx);
                d[nx] = d[tec] + 1;
                p[nx] = tec;
            }
        }
    }

    //Far node
    center = v;
    range = 0;
    for (int i = 0; i < n; ++i) {
        if (d[i] > range) {
            center = i;
            range = d[i];
        }
    }

    if (!ind) {
        return;
    }
    for (int i = 0; i < range / 2; ++i) {
        //cout << center << " ";
        center = p[center];
    }
}

long long dfs(int v, bool r) {
    was[v] = true;
    long long hash1 = 1;
    long long hash2 = 0;
    long long ch1, ch2;

    int kol = 0;

    for (int i = 0; i < gr[v].size(); ++i) {
        int nx = gr[v][i];
        if (!was[nx]) {
            ++kol;
            long long val = dfs(nx, false);
            hash1 *= val;
            hash2 += val;
            ch2 = ch1;
            ch1 = val;
        }
    }

    if (r) {
        ans = (kol == 2 && ch1 == ch2);
    }

    return hash1 + hash2;
}

int main() {
    cin.tie(0);
    cout.tie(0);

    cin >> n;
    gr.assign(n, vector<int>());
    was.assign(n, 0);

    int a, b;
    for (int i = 0; i < n - 1; ++i) {
        cin >> a >> b;
        gr[a - 1].push_back(b - 1);
        gr[b - 1].push_back(a - 1);
    }

    bfs(0, 0);
    was.assign(n, false);
    bfs(center, 1);
    if (range % 2 == 1 || gr[center].size() != 2) {
        cout << "NO";
        return 0;
    }

    was.assign(n, false);
    dfs(center, 1);

    cout << (ans ? "YES" : "NO");

    return 0;
}
