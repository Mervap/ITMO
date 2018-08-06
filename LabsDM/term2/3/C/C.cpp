#include <iostream>
#include <vector>
#include <set>

using namespace std;

struct ttt {
    char c;
    vector<char> to;
};

vector<ttt> q;
set<char> was;
set<char> t;
set<char> r;

void filter(set<char> &a) {
    vector<ttt> q1;
    for (auto u : q) {
        bool flag = a.count(u.c);

        for (char to : u.to) {
            if (!a.count(to)) {
                flag = false;
                break;
            }
        }

        if (flag) {
            q1.push_back(u);
        }
    }
    q = q1;
}


int main() {

    freopen("useless.in", "r", stdin);
    freopen("useless.out", "w", stdout);

    int n;
    string s;
    cin >> n >> s;
    was.insert(s[0]);

    string a, b;
    for (size_t i = 0; i < n; ++i) {
        cin >> a >> b;
        char v = a[0];
        was.insert(v);

        getline(cin, b);
        vector<char> to;

        bool add = true;
        for (char c : b) {
            if ('A' <= c && c <= 'Z') {
                was.insert(c);
                to.push_back(c);
                add = false;
            }
        }
        if (add) {
            t.insert(v);
        }
        q.push_back({v, to});
    }

    int sz = -1;
    while ((int) t.size() > sz) {
        sz = t.size();

        for (auto u : q) {
            bool f = true;
            for (char to : u.to) {
                if (!t.count(to)) {
                    f = false;
                    break;
                }
            }

            if (f) {
                t.insert(u.c);
            }
        }
    }
    filter(t);

    r.insert(s[0]);
    sz = -1;
    while ((int) r.size() > sz) {
        sz = r.size();

        for (auto u : q) {
            if (r.count(u.c)) {
                for (char to : u.to) {
                    if (!r.count(to)) {
                        r.insert(to);
                    }
                }
            }
        }
    }
    filter(r);

    for (char c : was) {
        bool f = false;
        for (auto u : q) {
            f |= (c == u.c);
            for (char to : u.to) {
                f |= (c == to);
            }
        }

        if (!f) {
            cout << c << " ";
        }
    }

    return 0;
}