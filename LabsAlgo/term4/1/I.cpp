#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <queue>
#include <cassert>

using namespace std;

struct Aho_Corasick {

private:

    static const int ALPHABET_SIZE = 26;

    struct Node {

        Node(Node *p, char c) : term(), p(p), cp(c - 'a'), ch(ALPHABET_SIZE, nullptr) {}

        vector<int> term;
        Node *p = nullptr;
        int cp = 0;
        vector<Node *> ch;
        Node *suff = nullptr;
        Node *cSuff = nullptr;
        bool check = false;
    };

    Node *root = nullptr;

    void addStringDfs(Node *v, int ind, const string &s, int stId) {

        if (ind == s.length()) {
            v->term.push_back(stId);
            return;
        }

        if (v->ch[s[ind] - 'a'] == nullptr) {
            v->ch[s[ind] - 'a'] = new Node(v, s[ind]);
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
                cerr << suff << " ";
            }

            if (suff->ch[cur->cp] != nullptr) {
                suff = suff->ch[cur->cp];
            }

            cur->suff = cur->cSuff = suff;
        }
    }

    Node *updateCSuff(Node *cur, vector<int> &tmp, int i) {
        if (cur == root || cur->check) {
            return root;
        }

        cur->check = true;

        cur->cSuff = updateCSuff(cur->cSuff, tmp, i);
        if (!cur->term.empty()) {
            for (auto e : cur->term) {
                tmp[e] = i;
            }
            return cur;
        } else {
            return cur->cSuff;
        }
    }

    Node *state;
    size_t cnt;

public:
    Aho_Corasick(const vector<string> &strings) : root(new Node(nullptr, 0)), state(root), cnt(strings.size()) {
        for (int i = 0; i < strings.size(); ++i) {
            addStringDfs(root, 0, strings[i], i);
        }

        initSuff();
    }

    vector<int> find(const string &text) {

        vector<int> tmp(cnt, -1);

        for (int i = 0; i < text.length(); ++i) {
            while (state != root && state->ch[text[i] - 'a'] == nullptr) {
                state = state->suff;
            }

            if (state->ch[text[i] - 'a'] != nullptr) {
                state = state->ch[text[i] - 'a'];
            }

            updateCSuff(state, tmp, i);
        }

        return tmp;
    }

};

int main() {

    freopen("dictionary.in", "r", stdin);
    freopen("dictionary.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);
    ios_base::sync_with_stdio(false);

    string t;
    cin >> t;

    int n;
    cin >> n;
    vector<string> s;
    s.resize(n);
    for (int i = 0; i < n; ++i) {
        cin >> s[i];
    }

    Aho_Corasick ac(s);

    auto ans = ac.find(t);
    for (int an : ans) {
        if (an != -1) {
            cout << "Yes\n";
        } else {
            cout << "No\n";
        }
    }

    return 0;
}