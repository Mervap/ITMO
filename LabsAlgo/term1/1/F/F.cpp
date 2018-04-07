#include <iostream>
#include <fstream>
#include <stdio.h>
#include <cstdlib>

using namespace std;

long long p;
long s = 10;
void push(long long k);
long long pop();

long long *arr;
long long *tmp;
bool f;

int main() {
    freopen("postfix.in", "r", stdin);
    freopen("postfix.out", "w", stdout);

    ios_base::sync_with_stdio(false);
    cout.tie(0);
    cin.tie(0);

    arr = (long long *) malloc(s * sizeof(long long));

    char c;
    long long a, b;
    while (cin >> c) {
        if (c == '+') {
            a = pop();
            b = pop();
            push(a + b);
        } else if (c == '*') {
            a = pop();
            b = pop();
            push(a * b);
        } else if (c == '-') {
            a = pop();
            b = pop();
            push(b - a);
        } else {
            push(c - '0');
        }
    }
    cout << pop();
    return 0;
}

void push(long long k) {
    if (p == s) {
        s = s * 2;
        tmp = arr;
        arr = (long long *) malloc(s * sizeof(long long));
        for (int i = 0; i < p; i++) {
            arr[i] = tmp[i];
        }
        free(tmp);
    }
    arr[p] = k;
    p++;
}

long long pop() {
    if (p <= s / 4) {
        s = s / 2;
        tmp = arr;
        arr = (long long *) malloc(s * sizeof(long long));
        for (int i = 0; i < p; i++) {
            arr[i] = tmp[i];
        }
        free(tmp);
    }
    p--;
    return arr[p];
}
