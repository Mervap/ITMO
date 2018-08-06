#include <iostream>

using namespace std;

struct node {
    int k;
    node *l;
    node *r;
    node *p;
};

node *root;

void ins(int x) {

    node *tec = root;
    node *nw = new node;
    nw->r = 0;
    nw->l = 0;
    nw->p = 0;

    nw->k = x;

    if (root == 0) {
        root = nw;
        return;
    }

    while (true) {
        if (tec->k == x) {
            delete nw;
            return;
        }
        if (x > tec->k) {
            if (tec->r == 0) {
                tec->r = nw;
                nw->p = tec;
                return;
            }
            tec = tec->r;
        } else {
            if (tec->l == 0) {
                tec->l = nw;
                nw->p = tec;
                return;
            }
            tec = tec->l;
        }
    }
}

node *del(node *tec, int x) {
    if (tec == 0) {
        return tec;
    }

    if (x < tec->k) {
        tec->l = del(tec->l, x);
        if (tec->l != 0) {
            tec->l->p = tec;
        }
    } else if (x > tec->k) {
        tec->r = del(tec->r, x);
        if (tec->l != 0) {
            tec->l->p = tec;
        }
    } else {
        if (tec->l != 0 && tec->r != 0) {
            node *nw = tec->r;
            while (nw->l != 0) {
                nw = nw->l;
            }
            tec->k = nw->k;
            tec->r = del(tec->r, tec->k);
        } else if (tec->l != 0) {
            node *nw = tec->l;
            if (nw != 0) {
                nw->p = tec->p;
            }
            delete tec;
            tec = nw;
        } else {
            node *nw = tec->r;
            if (nw != 0) {
                nw->p = tec->p;
            }
            delete tec;
            tec = nw;
        }
    }

    return tec;
}

node *exists(int x) {
    node *tec = root;
    while (tec != 0 && (tec->k) != x) {
        if (x > tec->k) {
            tec = tec->r;
        } else {
            tec = tec->l;
        }
    }

    return tec;
}

node *nx(int x) {
    node *tec = exists(x);
    if (tec->r == 0) {
        node *p = tec->p;
        while (p != 0 && tec == p->r) {
            tec = p;
            p = p->p;
        }
        return p;

    } else {
        tec = tec->r;
        while (tec->l != 0) {
            tec = tec->l;
        }
        return tec;
    }
}

node *prev(int x) {
    node *tec = exists(x);
    if (tec->l == 0) {
        node *p = tec->p;
        while (p != 0 && tec == p->l) {
            tec = p;
            p = p->p;
        }
        return p;

    } else {
        tec = tec->l;
        while (tec->r != 0) {
            tec = tec->r;
        }
        return tec;
    }
}

int main() {
    string s;// = "insert";
    int x;// = 5;
    while (cin >> s) {
        cin >> x;
        if (s == "insert") {
            ins(x);
            continue;
        }
        if (s == "delete") {
            root = del(root, x);
            continue;
        }
        if (s == "exists") {
            node *f = exists(x);
            if (f == 0) {
                cout << "false\n";
            } else {
                cout << "true\n";
            }
            continue;
        }
        if (s == "next") {
            bool was = exists(x);
            if (!was) {
                ins(x);
            }
            node *f = nx(x);
            if (f == 0) {
                cout << "none\n";
            } else {
                cout << (*f).k << "\n";
            }
            if (!was) {
                root = del(root, x);
            }
            continue;
        }
        if (s == "prev") {
            bool was = exists(x);
            if (!was) {
                ins(x);
            }
            node *f = prev(x);
            if (f == 0) {
                cout << "none\n";
            } else {
                cout << (*f).k << "\n";
            }
            if (!was) {
                root = del(root, x);
            }
            continue;
        }
    }
    return 0;
}
