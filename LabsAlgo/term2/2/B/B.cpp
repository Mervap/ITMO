#include <iostream>
#include <vector>

using namespace std;

int const N = 10000000;

struct node {
    string k, v;
    node *next;

    node(string &k, string &v) {
        this->k = k;
        this->v = v;
        this->next = 0;
    }
};

vector<node *> map(N, 0);

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

void insert(string k, string v) {
    int hash = getHash(k);
    node *tec = map[hash];
    if (tec == 0) {
        map[hash] = new node(k, v);
        return;
    }

    while (tec->next != 0 && tec->k != k) {
        tec = tec->next;
    }

    if (tec->k == k) {
        tec->v = v;
        return;
    }
    tec->next = new node(k, v);
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
        delete tec;
        return;
    }

    prev->next = tec->next;
    delete tec;
}

string exists(string k) {
    int hash = getHash(k);
    node *tec = map[hash];

    while (tec != 0 && tec->k != k) {
        tec = tec->next;
    }

    if (tec == 0) {
        return "none";
    } else {
        return tec->v;
    }
}

int main() {

    freopen("map.in", "r", stdin);
    freopen("map.out", "w", stdout);

    string s, k, v;
    int x;
    while (cin >> s) {
        cin >> k;
        if (s[0] == 'p') {
            cin >> v;
            insert(k, v);
        } else if (s[0] == 'd') {
            del(k);
        } else {
            printf("%s\n", exists(k).c_str());
        }
    }
    return 0;
}
