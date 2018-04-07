#include <iostream>
#include <fstream>
#include <cstdlib>

using namespace std;

int p;
void push(int k);
int pop();

int *a, *tmp;
int s = 10;

int main() {
    freopen("stack1.in", "r", stdin);
    freopen("stack1.out", "w", stdout);

    a = (int *) malloc(s * sizeof(int));

    p = 0;
    int n, k;
    char c;


    ios_base::sync_with_stdio(false);
    cout.tie(0);
    cin.tie(0);

    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> c;
        if (c == '+') {
            cin >> k;
            push(k);
        } else {
            cout << pop() << "\n";
        }
    }

    return 0;
}

void push(int k) {
    if (p == s) {
        s *= 2;
        tmp = a;
        a = (int *) malloc(s * sizeof(int));
        for (int i = 0; i < p; i++) {
            a[i] = tmp[i];
        }
        free(tmp);
    }
    a[p] = k;
    p++;
}

int pop() {
    if (p <= s / 4) {
        s /= 2;
        tmp = a;
        a = (int *) malloc(s * sizeof(int));
        for (int i = 0; i < p; i++) {
            a[i] = tmp[i];
        }
        free(tmp);
    }
    p--;
    return a[p];
}
