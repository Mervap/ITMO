#include <iostream>
#include <vector>
#include <queue>

using namespace std;

template<typename S, typename T>
struct Min_cost_max_flow {

private:
    struct edge {
        int b, e;
        S c, f, p;
        edge *back_edge;

        edge(int b, int e, S c, S p) : b(b), e(e), c(c), f(0), p(p), back_edge(nullptr) {}
    };

    vector<vector<edge *>> edges;
    vector<int> d;
    vector<edge *> from;

    S INF;
    int n;

    bool bfs(int s, int t) {
        d.assign(n, INF);
        d[s] = 0;
        for (int i = 0; i < n - 1; ++i) {
            for (int j = 0; j < n; ++j) {
                for (auto e : edges[j]) {
                    if (e->c - e->f == 0) {
                        continue;
                    }

                    if (d[j] < INF) {
                        if (d[e->e] > d[e->b] + e->p) {
                            d[e->e] = d[j] + e->p;
                            from[e->e] = e;
                        }
                    }
                }
            }
        }

        return d[t] != INF;
    }

    pair<T, T> push(int s, int v, S c_min) {
        if (v == s) {
            return {c_min, 0};
        }
        
        auto e = from[v];
        auto add = push(s, e->b, std::min(c_min, e->c - e->f));
        if (add.first > 0) {
            e->f += add.first;
            e->back_edge->f -= add.first;
            add.second += add.first * e->p;
        }
        return add;
    }

public:

    explicit Min_cost_max_flow(int n, S INF) : INF(INF), n(n) {
        d.resize(n);
        from.resize(n);
        edges.resize(n);
    }

    void add_edge(int b, int e, S c, S p) {
        edge *e1 = new edge(b, e, c, p);
        edge *e2 = new edge(e, b, 0, -p);
        e1->back_edge = e2;
        e2->back_edge = e1;
        edges[b].push_back(e1);
        edges[e].push_back(e2);
    }

    T run(int s, int t) {
        T price = 0;
        while (bfs(s, t)) {
            auto add = push(s, t, INF);
            price += add.second;
        }

        return price;
    }
};

int main() {
    ios_base::sync_with_stdio(false);
    int n, m;
    cin >> n >> m;

    Min_cost_max_flow<int, long long> min_cost(n, 1000000001);

    int b, e, c, p;
    for (int i = 0; i < m; ++i) {
        cin >> b >> e >> c >> p;
        min_cost.add_edge(b - 1, e - 1, c, p);
    }

    cout << min_cost.run(0, n - 1);

    return 0;
}