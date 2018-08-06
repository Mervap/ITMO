#include <iostream>
#include <vector>

using namespace std;

int const N = 10000000;

struct node {
    int k;
    node *next;

    node(int k) {
        this->k = k;
        this->next = 0;
    }
};

node* set[N];
//vector<node*> set(N,0);

void insert(int x) {
    int hash = x % N;
    node *tec = set[hash];
    if (tec == 0) {
        set[hash] = new node(x);
        return;
    }

    while (tec->next != 0 && tec->k != x) {
        tec = tec->next;
    }

    if (tec->k == x) {
        return;
    }
    tec->next = new node(x);
}

void del(int x) {
    int hash = x % N;
    node *tec = set[hash];
    node *prev = 0;

    while (tec != 0 && tec->k != x) {
        prev = tec;
        tec = tec->next;
    }

    if (tec == 0) {
        return;
    }

    if (prev == 0) {
        set[hash] = tec->next;
        delete tec;
        return;
    }

    prev->next = tec->next;
    delete tec;
}

bool exists(int x) {
    int hash = x % N;
    node *tec = set[hash];

    while (tec != 0 && tec->k != x) {
        tec = tec->next;
    }

    if (tec == 0) {
        return false;
    } else {
        return true;
    }
}

int main() {

    freopen("set.in", "r", stdin);
    freopen("set.out", "w", stdout);

    char s[8];
    int x;
    while (scanf("%s%d", s, &x) != EOF) {
        if (s[0] == 'i') {
            insert(x + 1e9);
        } else if (s[0] == 'd') {
            del(x + 1e9);
        } else {
            printf(exists(x + 1e9) ? "true\n" : "false\n");
        }
    }

    return 0;
}