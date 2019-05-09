#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <queue>
#include <cassert>

using namespace std;

const int MOD = 10000;

struct Aho_Corasick {

private:

    static const int ALPHABET_SIZE = 26;

    struct Node {

        Node(Node *p, char c, int cnt) : term(), p(p), cp(c - 'a'), ch(ALPHABET_SIZE, nullptr), id(cnt) {}

        vector<int> term;
        Node *p = nullptr;
        int cp = 0;
        vector<Node *> ch;
        Node *suff = nullptr;
        int id;
    };

    Node *root = nullptr;

    void addStringDfs(Node *v, int ind, const string &s, int stId) {

        if (ind == s.length()) {
            v->term.push_back(stId);
            return;
        }

        if (v->ch[s[ind] - 'a'] == nullptr) {
            v->ch[s[ind] - 'a'] = new Node(v, s[ind], cnt++);
        }

        addStringDfs(v->ch[s[ind] - 'a'], ind + 1, s, stId);
    }

    void initSuff() {
        queue<Node *> q;
        root->suff = root;
        q.push(root);

        while (!q.empty()) {
            auto cur = q.front();
            q.pop();

            for (int i = 0; i < ALPHABET_SIZE; ++i) {
                if (cur->ch[i] != nullptr) {
                    q.push(cur->ch[i]);
                }
            }

            if (cur == root || cur->p == root) {
                cur->suff = root;
                continue;
            }

            auto suff = cur->p->suff;
            while (suff != root && suff->ch[cur->cp] == nullptr) {
                suff = suff->suff;
            }

            if (suff->ch[cur->cp] != nullptr) {
                suff = suff->ch[cur->cp];
            }

            cur->suff = suff;
        }
    }

    bool check_term(Node *v) {
        if (!v->term.empty()) {
            return false;
        }

        if (v == root) {
            return true;
        }

        return check_term(v->suff);
    }


    void dfs(Node *v, vector<vector<int>> &tmp) {
        for (int i = 0; i < ALPHABET_SIZE; ++i) {
            auto ch = v->ch[i];
            if (ch != nullptr) {
                if (check_term(ch)) {
                    tmp[v->id][ch->id] += 1;
                    dfs(ch, tmp);
                    continue;
                }
            } else {
                auto suff = v->suff;
                while (suff != root && suff->ch[i] == nullptr) {
                    suff = suff->suff;
                }

                if (suff->ch[i] != nullptr) {
                    suff = suff->ch[i];
                }

                if (check_term(suff)) {
                    tmp[v->id][suff->id] += 1;
                }
            }
        }
    }

    Node *state;
    int cnt = 1;

public:
    Aho_Corasick(const vector<string> &strings) : root(new Node(nullptr, 0, 0)), state(root) {
        for (int i = 0; i < strings.size(); ++i) {
            addStringDfs(root, 0, strings[i], i);
        }

        initSuff();
    }

    vector<vector<int>> getMatrix() {
        vector<vector<int>> tmp(cnt, vector<int>(cnt, 0));
        dfs(root, tmp);
        return tmp;
    }

};

vector<vector<int>> mul(vector<vector<int>> a, vector<vector<int>> b) {

    int n = a.size();
    vector<vector<int>> bt(n, vector<int>(n)), c(n, vector<int>(n));

    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            bt[i][j] = b[j][i];
        }
    }
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            for (int k = 0; k < n; ++k) {
                c[i][j] += a[i][k] * bt[j][k];
                c[i][j] %= MOD;
            }
        }
    }

    return c;
}

vector<vector<int>> log_mul(vector<vector<int>> &a, int n) {
    if (n == 1) {
        return a;
    }

    if (n % 2 == 0) {
        auto aa = log_mul(a, n / 2);
        return mul(aa, aa);
    } else {
        auto aa = log_mul(a, n - 1);
        return mul(aa, a);
    }
}

int log_mul(int a, int n) {
    if (n == 1) {
        return a;
    }

    if (n % 2 == 0) {
        auto aa = log_mul(a, n / 2);
        return (aa * aa) % MOD;
    } else {
        return (log_mul(a, n - 1) * a) % MOD;
    }
}

int main() {

    cin.tie(0);
    cout.tie(0);
    ios_base::sync_with_stdio(false);

    int n;
    cin >> n;
    vector<string> s;
    s.resize(n);
    for (int i = 0; i < n; ++i) {
        cin >> s[i];
    }

    Aho_Corasick ac(s);
    auto a = ac.getMatrix();

    int k;
    cin >> k;

    auto ans = log_mul(a, k);

    int anss = 0;
    for (auto i : ans[0]) {
        anss += i;
        anss %= MOD;
    }

    int all = log_mul(26, k);

    cout << (all + MOD - anss) % MOD;
    return 0;
}