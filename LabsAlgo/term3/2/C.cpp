#include <iostream>
#include <vector>
#include <ctime>

using namespace std;

const int INF = 1000000000;

vector<vector<int>> d;
vector<bool> used;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n, m;
    cin >> n >> m;


    int a, b, w;
    d.assign(n, vector<int>(n, INF));

    for (int i = 0; i < n; ++i) {
        d[i][i] = 0;
    }

    for (int j = 0; j < m; ++j) {
        cin >> a >> b >> w;
        d[a - 1][b - 1] = w;
    }

    for (int k = 0; k < n; ++k) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                d[i][j] = min(d[i][j], d[i][k] + d[k][j]);
            }
        }
    }

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cout << d[i][j] << " ";
        }

        cout << "\n";
    }

    return 0;
}