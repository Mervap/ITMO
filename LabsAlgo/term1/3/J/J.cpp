#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <algorithm>

#define f first
#define s second

using namespace std;

vector<int> mes, p, r, change;

long long zerg = 0;
long long mx = 1000003;

pair<int, int> find_root(int i) {
    int z = 0;
    if (i < 0) {
        cout << "WTF";
    }
    if (p[i] != i) {
        pair<int, int> k = find_root(p[i]);
        p[i] = k.f;
        z = (k.s + change[i]);
        change[i] = z;
    } else {
        z = 0;
    }
    return make_pair(p[i], z);
}

/*int find_root(int i){
    if(p[i] != i){
        p[i] = find_root(p[i]);
    }
    return p[i];
}*/

void un(int i, int j) {
    int x = find_root(i).f;
    int y = find_root(j).f;

    if (x == y) {
        return;
    }

    zerg = (13 * zerg + 11) % mx;

    if (r[x] == r[y]) {
        ++r[x];
    }
    if (r[x] < r[y]) {
        p[x] = y;
        change[x] -= change[y];
    } else {
        p[y] = x;
        change[y] -= change[x];
    }
}

int main() {
    int m, n;
    cin >> n >> m;

    mes.assign(n, 0);
    p.assign(n, 0);
    r.assign(n, 0);
    change.assign(n, 0);
    for (int i = 0; i < n; ++i) {
        p[i] = i;
    }

    int t, a, b;
    for (int i = 0; i < m; ++i) {
        cin >> t;
        if (t == 1) {
            cin >> a;
            a = (int) (a + zerg) % n;
            b = find_root(a).f;
            ++change[b];
            zerg = (30 * zerg + 239) % mx;
        } else if (t == 2) {
            cin >> a >> b;
            a = (int) (a + zerg) % n;
            b = (int) (b + zerg) % n;
            un(a, b);
        } else {
            cin >> a;
            a = (int) (a + zerg) % n;
            b = find_root(a).f;

            long long q;
            if (a == b) {
                q = change[b] - mes[a];
                mes[a] = change[b];
            } else {
                q = (change[b] + change[a]) - mes[a];
                mes[a] = change[b] + change[a];
            }
            zerg = (100500 * zerg + q) % mx;
            cout << q << "\n";
        }
    }
    return 0;
}
