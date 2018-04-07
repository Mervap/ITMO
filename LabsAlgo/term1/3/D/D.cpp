#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

vector<int> p;

int find_root(int i) {
    if (p[i] != i) {
        p[i] = find_root(p[i]);
    }
    return p[i];
}

void un(int i, int j) {
    int x = find_root(i);
    int y = find_root(j);

    p[x] = y;
}

int main() {
    freopen("parking.in", "r", stdin);
    freopen("parking.out", "w", stdout);

    int n, m;
    scanf("%d", &n);
    p.assign(n + 1, 0);
    for (int i = 0; i <= n; ++i) {
        p[i] = i;
    }

    for (int i = 0; i < n; ++i) {
        scanf("%d", &m);
        int x = find_root(m);
        printf("%d ", x);
        if (x + 1 > n) {
            un(x, 1);
        } else {
            un(x, x + 1);
        }
    }

    return 0;
}
