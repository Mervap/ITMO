#include <iostream>
#include <fstream>
#include <string>
#include <cstring>
#include <cstdlib>

using namespace std;

int p;
void push(int k);
int pop();

int *a;
int *tmp;
int ss = 10;
bool f;

int main() {
    freopen("brackets.in", "r", stdin);
    freopen("brackets.out", "w", stdout);

    ios_base::sync_with_stdio(false);
    cout.tie(0);
    cin.tie(0);

    a = (int *) malloc(ss * sizeof(int));

    string s;
    while (cin >> s) {
        f = false;
        ss = 10;
        free(a);
        a = (int *) malloc(ss * sizeof(int));
        p = 0;

        int l = s.length();
        for (int i = 0; i < l; i++) {
            if (s[i] == '(') {
                push(1);
            } else if (s[i] == ')') {
                if (pop() != 1) {
                    f = true;
                    break;
                }
            } else if (s[i] == '[') {
                push(2);
            } else {
                if (pop() != 2) {
                    f = true;
                    break;
                }
            }
        }
        if ((f == false) && (p == 0)) {
            cout << "YES\n";
        } else {
            cout << "NO\n";
        }
    }
    return 0;
}

void push(int k) {
    if (p == ss) {
        ss = ss * 2;
        tmp = a;
        a = (int *) malloc(ss * sizeof(int));
        for (int i = 0; i < p; i++) {
            a[i] = tmp[i];
        }
        free(tmp);
    }
    a[p] = k;
    p++;
}

int pop() {

    if (p <= ss / 4) {
        ss = ss / 2;
        tmp = a;
        a = (int *) malloc(ss * sizeof(int));
        for (int i = 0; i < p; i++) {
            a[i] = tmp[i];
        }
        free(tmp);
    }
    p--;
    if (p < 0) {
        return 0;
    }
    return a[p];
}
