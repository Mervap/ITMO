#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <queue>

using namespace std;

int n, m;

struct el {
    int n, w;

    bool operator<(el const &other) const {
        return w > other.w || (w == other.w && n < other.n);
    }
};

set<el> w;
vector<bool> notInd;

int main() {
    ios_base::sync_with_stdio(false);

    freopen("cycles.in", "r", stdin);
    freopen("cycles.out", "w", stdout);

    cin >> n >> m;
    notInd.resize(1 << n);

    int ww;
    for (int i = 0; i < n; ++i) {
        cin >> ww;
        w.insert({i, ww});
    }

    queue<int> q;
    for (int i = 0; i < m; ++i) {
        int mi;
        cin >> mi;

        int a = 0, cur;
        for (int j = 0; j < mi; ++j) {
            cin >> cur;
            a |= (1 << (cur - 1));
        }

        notInd[a] = true;
        q.push(a);
    }

    while (!q.empty()) {
        int a = q.front();
        q.pop();

        for (int i = 0; i < n; ++i) {
            int b = a | (1 << i);
            if (!notInd[b]) {
                notInd[b] = true;
                q.push(b);
            }
        }
    }

    int a = 0;
    int ans = 0;
    for (auto i : w) {
        if (!notInd[a | (1 << i.n)]) {
            a |= (1 << i.n);
            ans += i.w;
        }
    }

    cout << ans;
    return 0;
}