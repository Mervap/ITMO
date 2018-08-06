#include <iostream>
#include <vector>

using namespace std;

int const N = 10000000;

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

vector<node *> map(N, 0);
node *last = 0;

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

node *find(string k) {
    int hash = getHash(k);
    node *tec = map[hash];

    while (tec != 0 && tec->k != k) {
        tec = tec->next;
    }

    return tec;
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

string exists(string k) {
    node *tec = find(k);

    if (tec == 0) {
        return "none";
    } else {
        return tec->v;
    }
}

string next(string k) {
    node *tec = find(k);

    if (tec == 0 || tec->nx == 0) {
        return "none";
    } else {
        return tec->nx->v;
    }
}

string prev(string k) {
    int hash = getHash(k);
    node *tec = map[hash];

    while (tec != 0 && tec->k != k) {
        tec = tec->next;
    }

    if (tec == 0 || tec->pr == 0) {
        return "none";
    } else {
        return tec->pr->v;
    }
}

int main() {

    freopen("linkedmap.in", "r", stdin);
    freopen("linkedmap.out", "w", stdout);

    string s, k, v;
    while (cin >> s) {
        cin >> k;
        if (s[1] == 'u') {
            cin >> v;
            insert(k, v);
        } else if (s[0] == 'd') {
            del(k);
        } else if (s[0] == 'g') {
            printf("%s\n", exists(k).c_str());
        } else if (s[0] == 'p') {
            printf("%s\n", prev(k).c_str());
        } else {
            printf("%s\n", next(k).c_str());
        }
    }
    return 0;
}
