#include <iostream>
#include <vector>
#include <set>

using namespace std;

int n, m = 0, k = 0;
bool f;

struct nnn {
    int f, s;
};

vector<vector<nnn>> comp(16);

void make() {
    for (int i = 1; i < n; i += 2) {
        comp[k].push_back({i, i + 1});
        ++m;
    }
}

int main() {

    cin >> n;
    k = 0;
    make();
    for (int i = 2; i < n; i *= 2) {

        ++k;
        for (int j = 1; j <= n; j += 2 * i) {
            for (int l = 0; j + 2 * i - 1 - l > j + l; ++l) {
                if (j + 2 * i - 1 - l <= n) {
                    comp[k].push_back({j + l, j + 2 * i - 1 - l});
                    ++m;
                }
            }
        }

        for (int j = i / 2; j > 0; j /= 2) {
            ++k;
            for (int j1 = 1; j1 <= j; ++j1) {
                for (int l = j1; l + j <= n; l += j * 2) {
                    comp[k].push_back({l, l + j});
                    ++m;
                }
            }
        }
    }

    ++k;
    printf("%d %d %d\n", n, m, max(0, k));
    for (int i = 0; i < k; ++i) {
        auto c = comp[i];
        cout << c.size() << " ";
        for (auto cc : c) {
            cout << cc.f << " " << cc.s << " ";
        }
        cout << "\n";
    }

    return 0;
}