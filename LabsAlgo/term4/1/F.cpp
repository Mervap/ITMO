#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>

using namespace std;

vector<int> prefix(string s) {
    vector<int> tmp;

    tmp.push_back(0);
    for (int i = 1; i < s.length(); ++i) {
        int cur = tmp[i - 1];
        while (cur > 0 && s[cur] != s[i]) {
            cur = tmp[cur - 1];
        }

        tmp.push_back(cur + (s[cur] == s[i]));
    }

    return tmp;
}

int main() {

    freopen("period.in", "r", stdin);
    freopen("period.out", "w", stdout);

    string s;
    cin >> s;

    int b = prefix(s)[s.length() - 1];
    int p = s.length() - b;
    cout << (s.length() % p == 0 ? p : s.length());

    return 0;
}