#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <queue>
#include <cassert>

using namespace std;

struct big_integer {
    big_integer() : data(1, 0) {}

    big_integer(int a) {
        if (a == 0) {
            data.assign(1, 0);
        } else {
            while (a != 0) {
                data.push_back(a % 10);
                a /= 10;
            }
        }
    }

    big_integer(vector<int> &&data) : data(std::move(data)) {}

    big_integer(big_integer const &other) : data(other.data) {}

    big_integer(big_integer &&other) : big_integer(std::move(other.data)) {}

    static vector<int> sum_vectors(vector<int> const &a, vector<int> const &b) {
        int c = 0;
        int m = min(a.size(), b.size());
        int n = max(a.size(), b.size());
        vector<int> ans(n);
        for (int i = 0; i < m; ++i) {
            int sum = (c + a[i]) + b[i];
            ans[i] = sum % 10;
            c = sum / 10;
        }

        if (a.size() < b.size()) {
            for (int i = m; i < n; ++i) {
                int sum = c + b[i];
                ans[i] = sum % 10;
                c = sum / 10;
            }
        } else {
            for (int i = m; i < n; ++i) {
                int sum = c + a[i];
                ans[i] = sum % 10;
                c = sum / 10;
            }
        }

        if (c != 0) {
            ans.push_back(c);
        }

        return ans;
    }

    friend big_integer operator+(big_integer const &a, big_integer const &b) {
        return big_integer(std::move(sum_vectors(a.data, b.data)));
    }

    big_integer &operator+=(big_integer const &other) {
        this->data = std::move(sum_vectors(this->data, other.data));
        return *this;
    }

    friend big_integer operator*(big_integer const &a, big_integer const &b) {
        vector<int> ans;

        for (int i = 0; i < b.data.size(); ++i) {
            int c = 0;
            vector<int> tmp(a.data.size() + i);
            for (int j = 0; j < a.data.size(); ++j) {
                int sum = a.data[j] * b.data[i] + c;
                tmp[j + i] = sum % 10;
                c = sum / 10;
            }

            if (c != 0) {
                tmp.push_back(c);
            }
            ans = std::move(sum_vectors(ans, tmp));
        }

        return big_integer(std::move(ans));
    }

    void print() {
        for (int i = data.size() - 1; i >= 0; --i) {
            cout << data[i];
        }
    }

private:
    vector<int> data;
};

struct Aho_Corasick {

private:

    const int ALPHABET_SIZE = 26;
    const map<char, int> mapper;

    struct Node {

        Node(Node *p, int c, int cnt, int size) : term(), p(p), cp(c), ch(size, nullptr), id(cnt) {}

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

        if (v->ch[mapper.at(s[ind])] == nullptr) {
            v->ch[mapper.at(s[ind])] = new Node(v, mapper.at(s[ind]), cnt++, ALPHABET_SIZE);
        }

        addStringDfs(v->ch[mapper.at(s[ind])], ind + 1, s, stId);
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
                    ++tmp[v->id][ch->id];
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
                    ++tmp[v->id][suff->id];
                }
            }
        }
    }

    Node *state;
    int cnt = 1;

public:
    Aho_Corasick(const vector<string> &strings, int size, map<char, int> &mapper) : ALPHABET_SIZE(size),
                                                                                    mapper(std::move(mapper)),
                                                                                    root(new Node(nullptr, 0, 0,
                                                                                                  ALPHABET_SIZE)),
                                                                                    state(root) {
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

int main() {
    freopen("censored.in", "r", stdin);
    freopen("censored.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);
    ios_base::sync_with_stdio(false);

    int n, m, p;
    cin >> n >> m >> p;
    string alf;
    cin >> alf;

    int cnt = 0;
    map<char, int> mapper;
    for (auto c : alf) {
        if (mapper.count(c) == 0) {
            mapper[c] = cnt++;
        }
    }

    vector<string> s;
    s.resize(p);
    for (int i = 0; i < p; ++i) {
        cin >> s[i];
    }

    Aho_Corasick ac(s, n, mapper);
    auto a = ac.getMatrix();

    vector<big_integer> ans;

    ans.emplace_back(1);
    for (int i = 1; i < a.size(); ++i) {
        ans.emplace_back(0);
    }

    for (int q = 0; q < m; ++q) {
        vector<big_integer> ans1(ans.size());
        for (int i = 0; i < ans.size(); ++i) {
            for (int j = 0; j < ans.size(); ++j) {
                ans1[j] += ans[i] * big_integer(a[i][j]);
            }
        }
        ans = std::move(ans1);
    }

    big_integer anss;
    for (auto i : ans) {
        anss += i;
    }

    anss.print();
    return 0;
}