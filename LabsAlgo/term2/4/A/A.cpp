#include <iostream>
#include <vector>
#include <set>

using namespace std;

int n, m, k;
bool f;

struct nnn {
    int f, s;
};

vector<nnn> comp;
vector<int> p;

void sort() {
    vector<int> p1 = p;
    for (auto c : comp) {
        if (p1[c.f] > p1[c.s]) {
            swap(p1[c.f], p1[c.s]);
        }
    }

    for (int i = 1; i < n; ++i) {
        if (p1[i] < p1[i - 1]) {
            f = false;
            return;
        }
    }
}

void check(int i) {
    if (i == n) {
        sort();
    } else {
        for (int j = 0; j < 2; ++j) {
            p[i] = j;
            check(i + 1);
            if (!f) {
                return;
            }
        }
    }
}


int main() {

    cin >> n >> m >> k;

    p.resize(n);
    int a, b, r;
    for (size_t i = 0; i < k; ++i) {
        cin >> r;
        for (size_t j = 0; j < r; ++j) {
            cin >> a >> b;
            if (b < a) {
                swap(a, b);
            }
            comp.push_back({a - 1, b - 1});
        }
    }

    f = true;
    check(0);

    if (!f) {
        cout << "No";
    } else {
        cout << "Yes";
    }

    return 0;
}