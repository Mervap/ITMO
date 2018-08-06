#include <iostream>
#include <vector>

using namespace std;

int n, m, a, u, v;
vector<int> lg;
vector<int> arr;
vector<vector<int>> st;

int main() {

    freopen("sparse.in", "r", stdin);
    freopen("sparse.out", "w", stdout);

    cin >> n >> m >> a >> u >> vertex;

    lg.resize(n + 1);
    arr.resize(n + 1);

    lg[1] = 0;
    for (size_t i = 2; i < n + 1; ++i) {
        lg[i] = lg[i / 2] + 1;
    }

    st.assign(n + 1, vector<int>(lg[n] + 1, 0));

    arr[0] = a;
    for (size_t i = 1; i < n; ++i) {
        arr[i] = (23 * arr[i - 1] + 21563) % 16714589;
    }

    for (size_t j = 0; j < lg[n] + 1; ++j) {
        for (size_t i = 0; i + (1 << j) <= n; ++i) {
            if (j == 0) {
                st[i][j] = arr[i];
                continue;
            }
            st[i][j] = min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
        }
    }

    int ans = 0;
    for (size_t i = 0; i < m; ++i) {
        if (i != 0) {
            u = ((17 * u + 751 + ans + 2 * i) % n) + 1;
            vertex = ((13 * vertex + 593 + ans + 5 * i) % n) + 1;
        }

        if (u > vertex) {
            int j = lg[u - vertex + 1];
            ans = min(st[vertex - 1][j], st[u - (1 << j)][j]);
        } else {
            int j = lg[vertex - u + 1];
            ans = min(st[u - 1][j], st[vertex - (1 << j)][j]);
        }
    }

    cout << u << " " << vertex << " " << ans;
    return 0;
}