#include <iostream>
#include <vector>
#include <algorithm>
#include <fstream>
#include <string>

using namespace std;

int main() {
    freopen("bwt.in", "r", stdin);
    freopen("bwt.out", "w", stdout);

    string s;
    cin >> s;
    vector<string> a;

    a.push_back(s);
    int l = s.length() - 1;

    for (int i = 0; i < l; ++i) {
        a.push_back(a[i][l] + a[i].substr(0, l));
    }

    sort(a.begin(), a.end());

    s = "";
    for (int i = 0; i <= l; ++i) {
        s += a[i][l];
    }
    cout << s << endl;
    return 0;
}
