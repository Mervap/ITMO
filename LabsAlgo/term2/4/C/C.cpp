#include <iostream>
#include <vector>
#include <set>

using namespace std;

int m = 0;
bool f;

struct nnn {
    int f, s;
};

vector<nnn> comp;

void make(int n, int ind, int val) {
    for (int i = 1; i < n; i += 2) {
        comp.push_back({i + (i >= ind) * val, i + 1 + (i + 1 >= ind) * val});
    }
}

void add_comp(int n, int ind, int val) {
    make(n, ind, val);
    for (int i = 2; i < n; i *= 2) {

        for (int j = 1; j <= n; j += 2 * i) {
            for (int l = 0; j + 2 * i - 1 - l > j + l; ++l) {
                if (j + 2 * i - 1 - l <= n) {
                    comp.push_back({j + l + (j + l >= ind) * val, j + 2 * i - 1 - l + (j + 2 * i - 1 - l >= ind) * val});
                }
            }
        }

        for (int j = i / 2; j > 0; j /= 2) {
            for (int j1 = 1; j1 <= j; ++j1) {
                for (int l = j1; l + j <= n; l += j * 2) {
                    comp.push_back({l + (l >= ind) * val, l + j + (l + j >= ind) * val});
                }
            }
        }
    }
}

int main() {

    int n;
    cin >> n;
    while (n != 0) {
        comp.resize(0);

        int a = 0, pos1 = -1, pos = -1;
        vector<int> zero;
        for (int i = 1; i <= n; ++i) {
            cin >> a;

            if (a == 0) {
                zero.push_back(i);
                pos = i;
            } else if (pos1 == -1) {
                pos1 = i;
            }
        }

        if (pos1 > pos || pos1 == -1) {
            cout << -1 << "\n";
        } else {
            for (auto v : zero) {
                if (v != pos) {
                    comp.push_back({v, pos});
                }
            }
            add_comp(n - 1, pos, 1);
            add_comp(n - zero.size(), 0, zero.size());

            printf("%d \n", comp.size());
            for (auto c : comp) {
                cout << c.f << " " << c.s << "\n";
            }
        }

        cin >> n;
    }

    return 0;
}