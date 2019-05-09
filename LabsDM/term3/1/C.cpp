#include <iostream>
#include <vector>

using namespace std;

struct lamp {
    int n;

    bool operator<(lamp const &other) {
        cout << "1 " << this->n << " " << other.n << "\n";
        cout.flush();
        string s;
        cin >> s;
        return s == "YES";
    }
};

vector<lamp> a;


void msort(int l, int r) {
    if (l >= r) {
        return;
    }

    int m = (r + l) / 2;
    msort(l, m);
    msort(m + 1, r);

    vector<lamp> b(r - l + 1);
    int j1, j2;
    j1 = l;
    j2 = m + 1;
    for (int i = l; i <= r; i++) {
        if (j1 <= m && (j2 > r || !(a[j2] < a[j1]))) {
            b[i - l] = a[j1];
            ++j1;
        } else {
            b[i - l] = a[j2];
            ++j2;
        }
    }

    for (int i = l; i <= r; i++) {
        a[i] = b[i - l];
    }
}

int main() {
    int n;
    cin >> n;

    for (int i = 0; i < n; i++) {
        a.push_back({i + 1});
    }

    msort(0, n - 1);

    for (int i = 1; i < n; i++) {
        if (a[i] < a[i - 1]) {
            for (int j = 0; j < n + 1; ++j) {
                cout << "0 ";
            }
            exit(0);
        }
    }

    cout << "0 ";
    for (int i = 0; i < n; ++i) {
        cout << a[i].n << " ";
    }
    return 0;
}