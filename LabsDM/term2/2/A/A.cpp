#include <iostream>
#include <algorithm>
#include <vector>

using namespace std;
const int M = 26;

int n, m, k;

vector<bool> T;
vector<vector<int>> edges;

int main() {

    freopen("problem1.in", "r", stdin);
    freopen("problem1.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);


    string s;
    cin >> s;
    cin >> n >> m >> k;
    ++n;

    T.assign(n, 0);
    edges.assign(n, vector<int>(M, 0));

    int a, b;
    char c;
    for (int i = 0; i < k; ++i) {
        cin >> a;
        T[a] = true;
    }

    for (int i = 0; i < m; ++i) {
        cin >> a >> b >> c;
        edges[a][c - 'a'] = b;
    }

    int pos = 1;
    for (char i : s) {
        pos = edges[pos][i - 'a'];
    }

    cout << (T[pos] ? "Accepts" : "Rejects");

    return 0;

}