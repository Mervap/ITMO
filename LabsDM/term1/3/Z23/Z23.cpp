#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main() {
    freopen("nextvector.in", "r", stdin);
    freopen("nextvector.out", "w", stdout);


    string s;
    cin >> s;

    int i = (int) s.length() - 1;

    while (i >= 0 && s[i] != '1') {
        --i;
    }

    if (i < 0) {
        cout << "-\n";
    } else {
        for (int j = 0; j < i; ++j) {
            cout << s[j];
        }
        cout << 0;
        for (int j = i + 1; j < (int) s.length(); ++j) {
            cout << 1;
        }
        cout << "\n";
    }


    i = (int) s.length() - 1;

    while (i >= 0 && s[i] == '1') {
        --i;
    }

    if (i < 0) {
        cout << "-";
    } else {
        for (int j = 0; j < i; ++j) {
            cout << s[j];
        }
        cout << 1;
        for (int j = i + 1; j < (int) s.length(); ++j) {
            cout << 0;
        }
    }

    return 0;
}
