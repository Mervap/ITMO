#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

vector<int> a, ans, from, pos;


void out(int i, int l) {
    if (i == 0) {
        cout << l << "\n";
        return;
    }
    out(from[i], l + 1);
    cout << a[i] << " ";
}

int main() {
    freopen("lis.in", "r", stdin);
    freopen("lis.out", "w", stdout);

    int n;
    cin >> n;

    a.assign(n + 1, 0);
    ans.assign(n + 1, 1000000001);
    from.assign(n + 1, 1000000001);
    pos.assign(n + 1, 0);
    ans[0] = -1000000001;

    for (int i = 1; i <= n; ++i) {
        cin >> a[i];
    }

    int mx = 0;
    for (int i = 1; i <= n; ++i) {
        int l = 0;
        int r = n + 1;
        while (r - l > 1) {
            int mid = (l + r) / 2;
            if (ans[mid] < a[i]) {
                l = mid;
            } else {
                r = mid;
            }
        }

        if (ans[r - 1] < a[i] && a[i] < ans[r]) {
            ans[r] = a[i];
            from[i] = pos[r - 1];
            pos[r] = i;
            mx = max(mx, r);
        }
    }

    out(pos[mx], 0);

    return 0;
}
