#include <iostream>
#include <vector>
#include <string>

using namespace std;

int main() {
    int n;
    cin >> n;
    vector<vector<int>> a;

    int k = 1;

    for (int i = 0; i < n; i++) {
        k *= 2;
    }

    vector<string> s(k);
    a.assign(k, vector<int>(k));

    for (int i = 0; i < k; i++) {
        cin >> s[i];
        cin >> a[0][i];
    }

    for (int i = 1; i < k; i++) {
        for (int j = 0; j < k - i; j++) {
            a[i][j] = a[i - 1][j] ^ a[i - 1][j + 1];
        }
    }

    for (int i = 0; i < k; i++) {
        cout << s[i] << " " << a[i][0] << "\n";
    }
    return 0;
}
