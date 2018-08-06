#include <iostream>
#include <vector>
#include <map>
#include <memory.h>

using namespace std;

const int MD = 1000000007;

struct ttt {
    bool l;
    char a, b;
};

int dp[26][100][100];
vector<vector<ttt>> gr;

string word;

long long ans(int v, int l, int r) {
    if (dp[v][l][r] == -1) {
        int res = 0;
        for (auto u : gr[v]) {
            if (u.l) {
                if (l + 1 == r && word[l] == u.a) {
                    ++res;
                    res %= MD;
                }
            } else {
                for (int i = l + 1; i < r; ++i) {
                    res = (ans(u.a - 'A', l, i) * ans(u.b - 'A', i, r) + res) % MD;
                }
            }
        }

        dp[v][l][r] = res;
    }

    return dp[v][l][r];
}

int main() {
    string buf;

    freopen("nfc.in", "r", stdin);
    freopen("nfc.out", "w", stdout);

    gr.assign(26, vector<ttt>(0));
    memset(dp, -1, sizeof(dp));

    string s;
    int n;
    cin >> n >> s;

    string a, b;
    for (size_t i = 0; i < n; ++i) {
        cin >> a >> b;
        int v = a[0] - 'A';

        getline(cin, b);

        ttt to;
        to.a = b[1];
        if (b.length() > 2) {
            to.b = b[2];
            to.l = false;
        } else {
            to.l = true;
        }

        gr[v].push_back(to);
    }

    cin >> word;
    cout << ans(s[0] - 'A', 0, word.length());

    return 0;
}