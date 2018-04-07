#include <iostream>
#include <vector>
#include <fstream>

using namespace std;
vector<int> a;
unsigned int cur = 0;

unsigned int nextRand24();

unsigned int m, c, b;
unsigned long long answer;
int n;

void msort(int l, int r);

void msort(int l, int r) {
    if (l >= r) {
        return;
    }
    int mid = (r + l) / 2;
    msort(l, mid);
    msort(mid + 1, r);
    vector<int> b(r - l + 1);

    int j1, j2;
    j1 = l;
    j2 = mid + 1;
    for (int i = l; i <= r; i++) {
        if (j1 <= mid && (j2 > r || (a[j1] <= a[j2]))) {
            b[i - l] = a[j1];
            j1++;
        } else {
            b[i - l] = a[j2];
            j2++;
            answer += mid - j1 + 1;
        }
    }

    for (int i = l; i <= r; i++) {
        a[i] = b[i - l];
    }
}

unsigned int nextRand24() {
    cur = cur * c + b;
    return cur >> 8;
}


int main() {
    freopen("invcnt.in", "r", stdin);
    freopen("invcnt.out", "w", stdout);

    cin >> n >> m >> c >> b;

    a.assign(n, 0);

    for (int i = 0; i < n; i++) {
        a[i] = nextRand24() % m;
    }

    msort(0, n - 1);

    cout << answer;
    return 0;
}
