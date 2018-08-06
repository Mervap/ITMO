#include <iostream>
#include <vector>
#include <set>

using namespace std;

struct t {
    vector<int> to;
    bool c;
};

vector<vector<t>> gr(26);
set<int> t;

bool check() {

    int sz = -1;

    while((int) t.size() > sz) {
        sz = t.size();
        for (int i = 0; i < 26; i++) {
            bool f = false;

            for (auto u : gr[i]) {
                bool f1 = true;

                f1 &= u.c;
                for (auto to : u.to) {
                    f1 &= t.count(to);
                }

                f |= f1;
            }

            if (f) {
                t.insert(i);
            }
        }
    }
}

int main() {

    freopen("epsilon.in", "r", stdin);
    freopen("epsilon.out", "w", stdout);

    int n;
    string s;
    cin >> n >> s;

    string a, b;
    for (size_t i = 0; i < n; ++i) {
        cin >> a >> b;
        getline(cin, b);

        vector<int> to;
        bool ttt = true;

        for (char c : b) {
            if (c >= 'a' && c <= 'z') {
                ttt = false;
            } else if (c >= 'A' && c <= 'Z') {
                to.push_back(c - 'A');
            }
        }

        gr[a[0] - 'A'].push_back({to, ttt});
    }

    check();

    for (int i : t) {
        cout << (char) (i + 'A') << " ";
    }
}