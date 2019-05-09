#include <iostream>
#include <vector>
#include <queue>

using namespace std;

template<typename S, typename T>
struct Dinitz {

private:
    struct edge {
        int to;
        S c, f;
        edge *back_edge;
        int n;

        edge(int to, S c, int n) : to(to), c(c), f(0), back_edge(nullptr), n(n) {}
    };

    vector<vector<edge *>> edges;
    vector<int> d;
    vector<int> head;
    vector<int> l;

    S INF;
    int n;

    bool bfs(int s, int t, S A) {
        d.assign(n, -1);
        queue<int> q;

        q.push(s);
        d[s] = 0;

        while (!q.empty()) {
            int v = q.front();
            q.pop();

            for (auto e : edges[v]) {
                if (d[e->to] == -1 && e->c - e->f >= A) {
                    d[e->to] = d[v] + 1;
                    q.push(e->to);
                }
            }
        }

        return d[t] != -1;
    }

    S dfs(int v, int t, S cMin, S A) {
        if (v == t) {
            return cMin;
        }

        while (head[v] < edges[v].size()) {
            edge *e = edges[v][head[v]];
            if (d[v] + 1 == d[e->to] && e->c - e->f >= A) {
                S add = dfs(e->to, t, std::min(cMin, e->c - e->f), A);
                if (add > 0) {
                    e->f += add;
                    e->back_edge->f -= add;
                    return add;
                }
            }

            ++head[v];
        }

        return 0;
    }

public:

    explicit Dinitz(int n, int m, S INF) : INF(INF), n(n) {
        d.resize(n);
        head.resize(n);
        edges.resize(n);
        l.resize(m);
    }

    void add_edge(int b, int e, S c, int i) {
        edge *e1 = new edge(e, c, i);
        edge *e2 = new edge(b, 0, -1);
        e1->back_edge = e2;
        e2->back_edge = e1;
        edges[b].push_back(e1);
        edges[e].push_back(e2);
        l[i] = c;
    }

    T dinitz(int s, int t) {
        S A = (1 << 30);
        T flow = 0;
        while (A > 0) {
            while (bfs(s, t, A)) {
                head.assign(n, 0);
                T push;
                while (push = dfs(s, t, INF, A)) {
                    flow += push;
                }
            }

            A /= 2;
        }

        return flow;
    }

    bool circ(vector<int> &ans) {

        for (auto e : edges[0]) {
            if (e->f != e->c) {
                return false;
            }
        }

        for (int i = 1; i < n - 1; ++i) {
            for (auto e : edges[i]) {
                if (e->n != -1 && e->to != n - 1) {
                    ans[e->n] = e->f + l[e->n];
                }
            }
        }

        return true;
    }
};

int main() {
    ios_base::sync_with_stdio(false);
    int n, m;
    cin >> n >> m;

    Dinitz<int, long long> dinitz(n + 2, m, 1000000001);

    int b, e, l, r;
    for (int i = 0; i < m; ++i) {
        cin >> b >> e >> l >> r;
        dinitz.add_edge(0, e, l, i);
        dinitz.add_edge(b, e, r - l, i);
        dinitz.add_edge(b, n + 1, l, i);
    }

    dinitz.dinitz(0, n + 1);
    vector<int> ans(m);
    if (dinitz.circ(ans)) {
        cout << "YES\n";
        for (auto i : ans) {
            cout << i << "\n";
        }
    } else {
        cout << "NO";
    }

    return 0;
}