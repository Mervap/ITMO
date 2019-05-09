#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <algorithm>

using namespace std;

vector<int> order;

//Blocking Flow by MKM
template<typename S, typename T>
struct FastBlockingFlow {

private:
    struct edge {
        int b, e;
        S c, f;
        int n;

        edge(int b, int e, S c, int n) : b(b), e(e), c(c), f(0), n(n) {}
    };

    struct vertex {
        int c;
        int n;

        bool operator<(const vertex &other) const {
            return c < other.c || (c == other.c && n < other.n);
        }
    };

    vector<set<edge *>> edges;
    vector<set<edge *>> back_edges;
    vector<int> d;
    vector<int> head;
    vector<int> c_in;
    vector<int> c_out;
    vector<int> c;
    set<vertex> cur_min;
    vector<int> ans;

    S INF;
    int n;

    void update(edge *e, int del) {
        c_out[e->b] -= del;
        c_in[e->e] -= del;
        cur_min.erase(cur_min.find({c[e->b], e->b}));
        cur_min.erase(cur_min.find({c[e->e], e->e}));
        c[e->b] = std::min(c_out[e->b], c_in[e->b]);
        c[e->e] = std::min(c_out[e->e], c_in[e->e]);
        cur_min.insert({c[e->b], e->b});
        cur_min.insert({c[e->e], e->e});
    }

    void erase(int v) {
        for (auto e : edges[v]) {
            update(e, e->c - e->f);
            back_edges[e->e].erase(e);
        }

        for (auto e : back_edges[v]) {
            update(e, e->c - e->f);
            edges[e->b].erase(e);
        }
    }

    void push_t(int u, int f) {
        vector<int> s(n);
        s[u] = f;

        for (int q = 0; q < n - 1; ++q) {
            int v = order[q];
            f = s[v];
            if (f == 0) {
                continue;
            }
            for (auto e : edges[v]) {
                int del = std::min(f, e->c - e->f);
                if (del == 0) {
                    continue;
                }
                f -= del;
                e->f += del;
                ans[e->n] += del;
                update(e, del);
                s[e->e] += del;
            }
        }
    }

    void push_s(int u, int f) {
        vector<int> s(n);
        s[u] = f;

        for (int q = n - 1; q > 0; --q) {
            int v = order[q];
            f = s[v];
            if (f == 0) {
                continue;
            }
            for (auto e : back_edges[v]) {
                int del = std::min(f, e->c - e->f);
                if (del == 0) {
                    continue;
                }
                f -= del;
                e->f += del;
                ans[e->n] += del;
                update(e, del);
                s[e->b] += del;
            }
        }
    }

public:

    explicit FastBlockingFlow(int n, int m, S INF) : INF(INF), n(n) {
        d.resize(n);
        head.resize(n);
        edges.resize(n);
        back_edges.resize(n);
        c_in.resize(n);
        c_out.resize(n);
        c.resize(n);
        ans.resize(m);
    }

    void add_edge(int b, int e, S c, int i) {
        c_in[e] += c;
        c_out[b] += c;
        edge *e1 = new edge(b, e, c, i);
        edges[b].insert(e1);
        back_edges[e].insert(e1);
    }

    void run() {
        c_in[order[0]] = INF;
        c_out[order[n - 1]] = INF;
        for (int q = 0; q < n; ++q) {
            int i = order[q];
            c[i] = std::min(c_in[i], c_out[i]);
            cur_min.insert({c[i], i});
        }

        while (!cur_min.empty()) {
            auto cur = *(cur_min.begin());

            if (cur.c == 0) {
                erase(cur.n);
                cur_min.erase(cur);
            } else {
                push_t(cur.n, cur.c);
                push_s(cur.n, cur.c);
            }
        }
    }

    void write_ans() {
        for (auto e : ans) {
            cout << e << "\n";
        }
    }
};

int main() {
    ios_base::sync_with_stdio(false);
    int n, m, l;
    cin >> n >> m >> l;

    vector<pair<int, int>> preOrder;
    int q;
    for (int i = 0; i < n; ++i) {
        cin >> q;
        preOrder.push_back({q, i});
    }

    sort(preOrder.begin(), preOrder.end());
    for (auto e: preOrder) {
        order.push_back(e.second);
    }

    FastBlockingFlow<int, long long> bf(n, m, 1000000001);

    int b, e, c;
    for (int i = 0; i < m; ++i) {
        cin >> b >> e >> c;
        bf.add_edge(b - 1, e - 1, c, i);
    }

    bf.run();
    bf.write_ans();

    return 0;
}