#include <iostream>
#include <fstream>
#include <cstdlib>

using namespace std;

int s = 10, p = 0;
void add(int k);
int pop();

int *a, *nextt, *tmp, *tmp1;

int main() {
    freopen("stack2.in", "r", stdin);
    freopen("stack2.out", "w", stdout);

    ios_base::sync_with_stdio(false);
    cout.tie(0);
    cin.tie(0);

    int n, k;
    char c;

    a = (int *) malloc(s * sizeof(int));
    nextt = (int *) malloc(s * sizeof(int));

    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> c;
        if (c == '+') {
            cin >> k;
            add(k);
        } else {
            cout << pop() << "\n";
        }
    }

    return 0;
}

void add(int k) {
    if (p >= s - 1) {
        s = s * 2;
        tmp = a;
        tmp1 = nextt;
        a = (int *) malloc(s * sizeof(int));
        nextt = (int *) malloc(s * sizeof(int));

        for (int i = 0; i <= p; i++) {
            a[i] = tmp[i];
            nextt[i] = tmp1[i];
        }
        free(tmp);
        free(tmp1);
    }
    p++;
    a[p] = k;
    nextt[p] = p - 1;
}

int pop() {

    if (p > 0 && p <= s / 4) {
        s = s / 2 + 1;
        tmp = a;
        tmp1 = nextt;
        a = (int *) malloc(s * sizeof(int));
        nextt = (int *) malloc(s * sizeof(int));

        for (int i = 0; i <= p; i++) {
            a[i] = tmp[i];
            nextt[i] = tmp1[i];
        }
        free(tmp);
        free(tmp1);
    }
    int z = a[p];
    p = nextt[p];
    return z;
}
