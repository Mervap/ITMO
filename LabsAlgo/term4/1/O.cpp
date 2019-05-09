#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <queue>
#include <cassert>

using namespace std;

struct HLD {
    friend class Aho_Corasick;

private:
    vector<int> nx;
    vector<int> size;
    vector<int> par;
    vector<int> chain;
    vector<int> chain_cnt;
    vector<int> top;
    vector<int> depth;
    vector<int> in;

    vector<vector<int>> g;

    int n;
    vector<int> tree;
    vector<int> last;
    vector<int> lastPref;

    void updateDO(int i, int l, int r, int lz, int rz) {
        if (lz > rz) {
            return;
        }

        if (l == lz && r == rz) {
            ++tree[i];
            return;
        }

        int m = (l + r) / 2;
        updateDO(2 * i, l, m, lz, min(m, rz)),
                updateDO(2 * i + 1, m + 1, r, max(m + 1, lz), rz);
    }

    int getDO(int i, int l, int r, int pos) {
        if (l == r) {
            return tree[i];
        }

        int m = (l + r) / 2;
        if (pos <= m) {
            return tree[i] + getDO(2 * i, l, m, pos);
        } else {
            return tree[i] + getDO(2 * i + 1, m + 1, r, pos);
        }
    }

    int dfs(int v, int p) {
        par[v] = p;
        size[v] = 1;
        for (int u : g[v]) {
            if (u != p) {
                depth[u] = depth[v] + 1;
                size[v] += dfs(u, v);

                if (nx[v] == -1 || size[u] > size[nx[v]]) {
                    nx[v] = u;
                }
            }
        }

        return size[v];
    }

    int t = 0, cnt = 0;

    void hld(int v, int p) {
        chain[v] = cnt;
        in[v] = t++;
        if (chain_cnt[cnt] == 0) {
            top[cnt] = v;
        }

        ++chain_cnt[cnt];
        if (nx[v] != -1) {
            hld(nx[v], v);
        }

        for (int u : g[v]) {
            if (u == p || u == nx[v]) {
                continue;
            }
            ++cnt;
            hld(u, v);
        }
    }

public:
    int update(int a, int b, int c) {
        int res = 0;
        while (chain[a] != chain[b]) {
            if (depth[top[chain[a]]] < depth[top[chain[b]]]) {
                swap(a, b);
            }

            int start = top[chain[a]];
            if (last[chain[a]] != c) {
                last[chain[a]] = c;
                lastPref[chain[a]] = in[a];
                updateDO(1, 0, n - 1, in[start], in[a]);
            } else if (in[a] > lastPref[chain[a]]) {
                updateDO(1, 0, n - 1, lastPref[chain[a]] + 1, in[a]);
                lastPref[chain[a]] = in[a];
            }

            a = par[start];
        }

        if (depth[a] > depth[b]) {
            swap(a, b);
        }

        assert(a == top[chain[a]]);
        int start = a;
        if (last[chain[a]] != c) {
            last[chain[a]] = c;
            lastPref[chain[a]] = in[b];
            updateDO(1, 0, n - 1, in[a], in[b]);
        } else if (in[b] > lastPref[chain[a]]) {
            updateDO(1, 0, n - 1, lastPref[chain[a]] + 1, in[b]);
            lastPref[chain[a]] = in[b];
        }
        return res;
    }

    int get(int a) {
        return getDO(1, 0, n - 1, in[a]);
    }

    HLD(int n, vector<vector<int>> &g) : nx(n, -1), size(n), par(n), chain(n), chain_cnt(n), top(n), depth(n), in(n),
                                         g(std::move(g)), n(n), tree(4 * n), last(n, -1), lastPref(n, 0) {
        dfs(0, 0);
        hld(0, -1);
    }
};

struct Aho_Corasick {

private:

    static const int ALPHABET_SIZE = 26;

    struct Node {

        Node(Node *p, char c, int id) : term(), p(p), cp(c - 'a'), ch(ALPHABET_SIZE, nullptr), id(id) {}

        vector<int> term;
        Node *p = nullptr;
        int cp = 0;
        vector<Node *> ch;
        Node *suff = nullptr;
        Node *cSuff = nullptr;
        int id;
        bool check = false;
    };

    Node *root = nullptr;

    void addStringDfs(Node *v, int ind, const string &s, int stId) {

        if (ind == s.length()) {
            v->term.push_back(stId);
            return;
        }

        if (v->ch[s[ind] - 'a'] == nullptr) {
            v->ch[s[ind] - 'a'] = new Node(v, s[ind], ids++);
        }

        addStringDfs(v->ch[s[ind] - 'a'], ind + 1, s, stId);
    }

    void initSuff() {
        queue<Node *> q;
        root->cSuff = root->suff = root;
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
                cur->cSuff = cur->suff = root;
                continue;
            }

            auto suff = cur->p->suff;
            while (suff != root && suff->ch[cur->cp] == nullptr) {
                suff = suff->suff;
            }

            if (suff->ch[cur->cp] != nullptr) {
                suff = suff->ch[cur->cp];
            }

            cur->suff = cur->cSuff = suff;
        }
    }

    Node *updateCSuff(Node *cur) {
        if (cur == root || cur->check) {
            if (!cur->term.empty()) {
                return cur;
            } else {
                return cur->cSuff;
            }
        }

        cur->check = true;

        cur->cSuff = updateCSuff(cur->cSuff);
        if (!cur->term.empty()) {
            return cur;
        } else {
            return cur->cSuff;
        }
    }

    Node *state;
    size_t cnt;
    int ids = 1;
    vector<bool> check;

    void compress_dfs(Node *v) {
        updateCSuff(v);

        for (int i = 0; i < ALPHABET_SIZE; ++i) {
            if (v->ch[i] != nullptr) {
                compress_dfs(v->ch[i]);
            }
        }
    }

    void dfsSuffTree(Node *v, map<int, int> &mapp, map<int, int> &mappp, int &cnt) {
        if (!v->term.empty()) {
            mapp[v->id] = cnt++;
            for (auto e : v->term) {
                mappp[e] = mapp[v->id];
            }
        }

        for (int i = 0; i < ALPHABET_SIZE; ++i) {
            if (v->ch[i] != nullptr) {
                dfsSuffTree(v->ch[i], mapp, mappp, cnt);
            }
        }
    }

    void makeSuffTree(Node *v, map<int, int> &mapp, vector<vector<int>> &g) {
        if (!v->term.empty()) {
            g[mapp[v->cSuff->id]].push_back(mapp[v->id]);
        }

        for (int i = 0; i < ALPHABET_SIZE; ++i) {
            if (v->ch[i] != nullptr) {
                makeSuffTree(v->ch[i], mapp, g);
            }
        }
    }

public:
    Aho_Corasick(const vector<string> &strings) : root(new Node(nullptr, 0, 0)), state(root),
                                                  cnt(strings.size()) {
        for (int i = 0; i < strings.size(); ++i) {
            addStringDfs(root, 0, strings[i], i);
        }

        initSuff();
    }

    void compressSuff() {
        compress_dfs(root);
    }

    map<int, int> mapp;

    vector<vector<int>> getSuffTree(map<int, int> &mappp) {
        int cnt = 0;

        mapp[root->id] = cnt++;
        dfsSuffTree(root, mapp, mappp, cnt);

        vector<vector<int>> g(cnt);
        makeSuffTree(root, mapp, g);

        return g;
    }

    void update(const string &text, HLD &hld, int c) {

        state = root;

        for (int i = 0; i < text.length(); ++i) {
            while (state != root && state->ch[text[i] - 'a'] == nullptr) {
                state = state->suff;
            }

            if (state->ch[text[i] - 'a'] != nullptr) {
                state = state->ch[text[i] - 'a'];
            }

            auto e = state;
            if (e->term.empty()) {
                e = e->cSuff;
            }

            hld.update(mapp[e->id], mapp[root->id], c);
        }
    }

};

int main() {

    freopen("divljak.in", "r", stdin);
    freopen("divljak.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);
    ios_base::sync_with_stdio(false);

    int n;
    cin >> n;

    vector<string> texts;
    texts.resize(n);
    for (int i = 0; i < n; ++i) {
        cin >> texts[i];
    }

    Aho_Corasick ac(texts);
    ac.compressSuff();

    map<int, int> numbers;
    auto g = ac.getSuffTree(numbers);

    HLD hld(g.size(), g);
    int m;
    cin >> m;

    vector<int> q;
    for (int i = 0; i < m; ++i) {
        int a;
        cin >> a;
        if (a == 1) {
            string s;
            cin >> s;
            ac.update(s, hld, i);
        } else {
            cin >> a;
            cout << hld.get(numbers[a - 1]) << "\n";
        }
    }

    return 0;
}