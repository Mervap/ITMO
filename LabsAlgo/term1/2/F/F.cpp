#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int main() {
    freopen("antiqs.in", "r", stdin);
    freopen("antiqs.out", "w", stdout);

    int n;
    cin >> n;

    vector<int> a(n);
    for (int i = 0; i < n; i++) {
        a[i] = i + 1;
    }

    for (int i = 0; i < n; i++) {
        int tmp = a[i / 2];
        a[i / 2] = a[i];
        a[i] = tmp;
    }

    for (int i = 0; i < n; i++) {
        cout << a[i] << " ";
    }
    return 0;
}
