#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <memory.h>

using namespace std;

struct ttt {
    bool t;
    int to;
};

int dp[1001][100][100];
map<int, vector<vector<ttt>>> gr;

string word;

int s;

void remove_long() {
    map<int, vector<vector<ttt>>> gr1;
    int cnt = 26;
    for (auto &v : gr) {
        int i = v.first;

        for (auto &u : gr[i]) {
            if (u.size() <= 2) {
                gr1[i].push_back(u);
            } else {
                int pr = i;
                for (int j = 0; j < u.size() - 2; ++j) {
                    gr1[pr].push_back({u[j], {false, ++cnt}});
                    pr = cnt;
                }

                gr1[pr].push_back({u[u.size() - 2], u[u.size() - 1]});
            }
        }
    }
    gr = gr1;
}

set<int> del;

void find_eps() {

    int sz = -1;
    while ((int) del.size() > sz) {
        sz = del.size();
        for (auto &v : gr) {
            int i = v.first;

            bool f = false;
            for (auto &u : gr[i]) {
                bool f1 = true;

                for (auto to : u) {
                    if (to.t) {
                        f1 = false;
                    } else {
                        f1 &= del.count(to.to);
                    }
                }

                f |= f1;
            }

            if (f) {
                del.insert(i);
            }
        }
    }
}

void remove_e() {
    find_eps();

    map<int, vector<vector<ttt>>> gr1;
    for (auto &v : gr) {
        int i = v.first;
        for (auto &u : gr[i]) {
            if (u.size() == 1) {
                gr1[i].push_back(u);
            } else if (u.size() == 2){
                if (del.count(u[0].to) && del.count(u[1].to)) {
                    gr1[i].push_back(u);
                    gr1[i].push_back({u[0]});
                    gr1[i].push_back({u[1]});
                } else if (del.count(u[0].to)) {
                    gr1[i].push_back({u[1]});
                    gr1[i].push_back({u[0], u[1]});
                } else if (del.count(u[1].to)) {
                    gr1[i].push_back({u[0]});
                    gr1[i].push_back({u[0], u[1]});
                } else {
                    gr1[i].push_back(u);
                }
            }
        }
    }

    if (del.count(s)) {
        gr1[1000].push_back({{false, s}});
        gr1[1000].push_back({{}});
        s = 1000;
    }
    gr = gr1;
}

int ans(int v, int l, int r) {
    if (dp[v][l][r] == -2 || l >= r) {
        return 0;
    }

    if (dp[v][l][r] == -1) {
        int res = 0;
        dp[v][l][r] = -2;

        for (auto &u : gr[v]) {
            if (u.size() == 1) {
                if (u[0].t) {
                    if (l + 1 == r && word[l] == u[0].to) {
                        res = 1;
                    }
                } else {
                    res |= ans(u[0].to, l, r);
                }
            } else if (u.size() == 2) {
                if (u[0].t && u[1].t) {
                    if (l + 2 == r && word[l] == u[0].to && word[l + 1] == u[1].to) {
                        res = 1;
                    }
                } else if (u[0].t) {
                    if (word[l] == u[0].to) {
                        res |= ans(u[1].to, l + 1, r);
                    }
                } else if (u[1].t) {
                    if (word[r - 1] == u[1].to) {
                        res |= ans(u[0].to, l, r - 1);
                    }
                } else {
                    for (int i = l + 1; i < r; ++i) {
                        res |= ans(u[0].to, l, i) * ans(u[1].to, i, r);
                    }
                }
            }
        }

        dp[v][l][r] = res;
    }

    return dp[v][l][r];
}

int main() {

    freopen("cf.in", "r", stdin);
    freopen("cf.out", "w", stdout);

    memset(dp, -1, sizeof(dp));

    string ss;
    int n;
    cin >> n >> ss;

    s = ss[0] - 'A';

    string a, b;
    for (size_t i = 0; i < n; ++i) {
        cin >> a >> b;
        int v = a[0] - 'A';

        getline(cin, b);

        vector<ttt> to;
        for (auto u : b) {
            if (u >= 'A' && u <= 'Z') {
                to.push_back({false, u - 'A'});
            } else if (u >= 'a' && u <= 'z') {
                to.push_back({true, u});
            }
        }
        gr[v].push_back(to);
    }

    remove_long();
    remove_e();

    cin >> word;
    cout << (ans(s, 0, word.length()) ? "yes" : "no");

    return 0;
}