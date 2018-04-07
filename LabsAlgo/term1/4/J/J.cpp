#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

string s;
string s1;
vector<pair<int, int> > f;
vector<pair<int, int> > g;

int n, m;

string ans(int st_a, int fn_a, int st_b, int fn_b) {

    if (st_a > fn_a || st_b > fn_b) {
        return "";
    }

    if (st_a == fn_a) {
        for (int i = st_b; i <= fn_b; ++i) {
            if (s[st_a - 1] == s1[i - 1]) {
                string s2 = "";
                s2 += s[st_a - 1];
                return s2;
            }
        }
        return "";
    }

    if (st_b == fn_b) {
        for (int i = st_a; i <= fn_a; ++i) {
            if (s1[st_b - 1] == s[i - 1]) {
                string s2 = "";
                s2 += s1[st_b - 1];
                return s2;
            }
        }
        return "";
    }

    int d = (fn_a + st_a) / 2;
    f.assign(m + 2, make_pair(0, 0));
    g.assign(m + 2, make_pair(0, 0));

    for (int i = st_a; i <= d; ++i) {
        f[0].second = 0;
        for (int j = st_b; j <= fn_b; ++j) {
            f[j].first = f[j].second;
            if (s[i - 1] == s1[j - 1]) {
                f[j].second = f[j - 1].first + 1;
            } else {
                if (f[j - 1].second > f[j].first) {
                    f[j].second = f[j - 1].second;
                } else {
                    f[j].second = f[j].first;
                }
            }
        }
    }

    for (int i = fn_a; i > d; --i) {
        g[0].second = 0;
        for (int j = fn_b; j >= st_b; --j) {
            g[j].first = g[j].second;
            if (s[i - 1] == s1[j - 1]) {
                g[j].second = g[j + 1].first + 1;
            } else {
                if (g[j + 1].second > g[j].first) {
                    g[j].second = g[j + 1].second;
                } else {
                    g[j].second = g[j].first;
                }
            }
        }
    }

    int mx = -1;
    int j = -1;
    for (int i = st_b - 1; i <= fn_b; ++i) {
        if (f[i].second + g[i + 1].second >= mx) {
            mx = f[i].second + g[i + 1].second;
            j = i;
        }
    }

    string z = ans(st_a, d, st_b, j);
    return z + ans(d + 1, fn_a, j + 1, fn_b);
}


int main() {

    //freopen("lis.in", "r", stdin);
    //freopen("lis.out", "w", stdout);

    cin >> s >> s1;

    n = (int) s.length();
    m = (int) s1.length();

    string t = ans(1, n, 1, m);
    cout << t << "\n";
    return 0;
}
