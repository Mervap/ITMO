#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <map>

using namespace std;

vector<vector<int>> edg;
vector<int> d;

int main() {
    int n, m;
    cin >> n >> m;

    edg.resize(n);
    d.assign(n, 1000000000);

    int a, b;
    for (int i = 0; i < m; ++i) {
        cin >> a >> b;
        --a, --b;
        edg[a].push_back(b);
        edg[b].push_back(a);
    }

    d[0] = 0;
    queue<int> q;
    q.push(0);

    while (!q.empty()) {
        int v = q.front();
        q.pop();

        for (auto e : edg[v]) {
            if (d[e] > d[v] + 1) {
                d[e] = d[v] + 1;
                q.push(e);
            }
        }
    }

    for (auto i : d) {
        cout << i << " ";
    }
    return 0;
}