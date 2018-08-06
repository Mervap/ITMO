#include <iostream>
#include <vector>

using namespace std;

FILE *f;
int const N = 10007;

struct hashMap {
private:
    struct node {
        string k, v;
        node *next;
        node *nx, *pr;

        node(string &k, string &v) {
            this->k = k;
            this->v = v;
            this->next = 0;
            this->nx = 0;
            this->pr = 0;
        }
    };

    int const N = 1000;
    vector<node *> map;
    node *last;

    int getHash(string s) {
        int p = 1;
        int ans = 0;
        for (int i = 0; i < s.length(); ++i) {
            ans += (s[i] * p) % N;
            ans %= N;
            p *= 31;
            p %= N;
        }

        return ans;
    }

public:
    hashMap() {
        map.assign(N, 0);
        last = 0;
    }

    void insert(string k, string v) {
        int hash = getHash(k);
        node *tec = map[hash];
        if (tec == 0) {
            map[hash] = new node(k, v);
            tec = map[hash];
        } else {
            while (tec->next != 0 && tec->k != k) {
                tec = tec->next;
            }

            if (tec->k == k) {
                tec->v = v;
                return;
            }
            tec->next = new node(k, v);
            tec = tec->next;
        }

        if (last != 0) {
            last->nx = tec;
            tec->pr = last;
        }

        last = tec;
    }

    void del(string k) {
        int hash = getHash(k);
        node *tec = map[hash];
        node *prev = 0;

        while (tec != 0 && tec->k != k) {
            prev = tec;
            tec = tec->next;
        }

        if (tec == 0) {
            return;
        }

        if (prev == 0) {
            map[hash] = tec->next;
        } else {
            prev->next = tec->next;
        }

        if (last == tec) {
            last = tec->pr;
            if (last != 0) {
                last->nx = 0;
            }
        } else {
            if (tec->pr != 0) {
                tec->pr->nx = tec->nx;
            }

            if (tec->nx != 0) {
                tec->nx->pr = tec->pr;
            }
        }
        delete tec;
    }

    string get(string k) {
        node *tec = last;
        int z = 0;
        string ans = "";
        while (tec != 0) {
            if (tec->v == k) {
                ++z;
                ans += tec->k + " ";
            }
            tec = tec->pr;
        }

        ans = to_string(z) + " " + ans;
        return ans;
    }

    void delAll(string k) {
        node *tec = last;
        while (tec != 0) {
            if (tec->v == k) {
                node *pr = tec->pr;
                node *nx = tec->nx;

                if (pr != 0) {
                    pr->nx = nx;
                }

                if (nx != 0) {
                    nx->pr = pr;
                }

                if (tec == last) {
                    last = tec->pr;
                }
            }
            tec = tec->pr;
        }
    }
};

vector<hashMap> map(N);
vector<int> p(20);

int getHash(string s) {
    int ans = 0;
    for (int i = 0; i < s.length(); ++i) {
        ans += (s[i] * p[i]) % N;
        ans %= N;
    }

    return ans;
}

void insert(string k, string v) {
    int hash = getHash(k);
    map[hash].insert(v, k);
}

void del(string k, string v) {
    int hash = getHash(k);
    map[hash].del(v);
}

void delAll(string k) {
    int hash = getHash(k);
    map[hash].delAll(k);
}

void exists(string k) {
    int hash = getHash(k);
    printf("%s\n", map[hash].get(k).c_str());
}

bool get(string &s) {
    char c;
    s = "";
    while ((c = getc(f)) != EOF && ((c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A'))) {
        s += c;
    }
    if (s != "") {
        return true;
    } else {
        return false;
    }
}

int main() {

    p[0] = 1;
    for (int i = 1; i < 20; ++i) {
        p[i] = p[i - 1] * 31;
        p[i] %= N;
    }

    f = fopen("multimap.in", "r");
    freopen("multimap.out", "w", stdout);

    string s, k, v;
    while (get(s)) {
        get(k);
        if (s[0] == 'p') {
            get(v);
            insert(k, v);
        } else if (s.length() == 6) {
            get(v);
            del(k, v);
        } else if (s[0] == 'd') {
            delAll(k);
        } else {
            exists(k);
        }
    }
    return 0;
}