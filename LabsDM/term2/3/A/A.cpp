#include <iostream>
#include <vector>

using namespace std;

struct t {
    int to;
    char c;
};

vector<vector<t>> gr(26);

bool check(int t, int pos, string &s) {
    if (pos == s.length()) {
        return (t == -1);
    }

    if (t == -1) {
        return false;
    }

    for (auto u : gr[t]) {
        if (u.c == s[pos]) {
            if (check(u.to, pos + 1, s)) {
                return true;
            }
        }
    }

    return false;
}

int main() {

    freopen("automaton.in", "r", stdin);
    freopen("automaton.out", "w", stdout);

    int n;
    string s;
    cin >> n >> s;

    string a, b;
    int to, c;
    for (size_t i = 0; i < n; ++i) {
        cin >> a >> b >> b;
        gr[a[0] - 'A'].push_back({b.length() == 1 ? -1 : b[1] - 'A', b[0]});
    }

    int m;
    cin >> m;
    for (size_t i = 0; i < m; ++i) {
        cin >> a;
        cout << (check(s[0] - 'A', 0, a) ? "yes" : "no") << "\n";
    }
}