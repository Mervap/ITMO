#include <iostream>
#include <vector>

using namespace std;

unsigned int a, b, m, q, cur = 0;
const unsigned int n = (1 << 24) + 1;

vector<unsigned int> arr(n);

unsigned nextRand() {
    cur *= a;
    cur += b;
    return cur >> 8;
}

int main() {

    freopen("fastadd.in", "r", stdin);
    freopen("fastadd.out", "w", stdout);

    cin >> m >> q >> a >> b;

    unsigned int add, l, r;

    for (size_t i = 0; i < m; ++i) {
        add = nextRand();
        l = nextRand();
        r = nextRand();
        if (l > r) {
            swap(l, r);
        }

        arr[l] += add;
        arr[r + 1] -= add;
    }


    for (size_t j = 0; j < 2; ++j) {
        for (size_t i = 1; i < n; ++i) {
            arr[i] += arr[i - 1];
        }
    }

    unsigned int ans = 0;
    for (size_t i = 0; i < q; ++i) {
        l = nextRand();
        r = nextRand();
        if (l > r) {
            swap(l, r);
        }
        ans += arr[r] - (l == 0 ? 0 : arr[l - 1]);
    }

    cout << ans;
    return 0;
}