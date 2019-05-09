#include <iostream>
#include <vector>
#include <queue>
#include <stack>

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

    struct path {
        int f = 0;
        vector<int> edg;

        path(int f, vector<int> &edg) : f(f), edg(std::move(edg)) {}
    };

    vector<vector<edge *>> edges;
    vector<int> d;
    vector<int> head;

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

    path sub_decompose(int s, int t) {
        vector<edge *> st;
        vector<int> was(n);

        int v = s;
        while (!was[v]) {
            if (v == t) {
                break;
            }

            was[v] = true;
            for (auto e : edges[v]) {
                if (e->n != -1 && e->f > 0 && !was[e->to]) {
                    st.push_back(e);
                    v = e->to;
                    break;
                }
            }
        }

        vector<int> edg;
        int f_min = INF;
        for (int i = 0; i < st.size(); ++i) {
            f_min = std::min(f_min, st[i]->f);
            edg.push_back(st[i]->n);
        }

        path p = path(f_min, edg);
        for (int i = 0; i < st.size(); ++i) {
            st[i]->f -= f_min;
        }

        return std::move(p);
    }

public:

    explicit Dinitz(int n, S INF) : INF(INF), n(n) {
        d.resize(n);
        head.resize(n);
        edges.resize(n);
    }

    void add_edge(int b, int e, S c, int i) {
        edge *e1 = new edge(e, c, i);
        edge *e2 = new edge(b, 0, -1);
        e1->back_edge = e2;
        e2->back_edge = e1;
        edges[b].push_back(e1);
        edges[e].push_back(e2);
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

    vector<path> decompose(int s, int t) {
        vector<path> ans;
        path p = sub_decompose(s, t);
        while (!p.edg.empty()) {
            ans.push_back(p);
            p = sub_decompose(s, t);
        }

        return std::move(ans);
    }
};

int main() {
    ios_base::sync_with_stdio(false);
    int n, m;
    cin >> n >> m;

    Dinitz<int, long long> dinitz(n, 1000000001);

    int b, e, c;
    for (int i = 0; i < m; ++i) {
        cin >> b >> e >> c;
        dinitz.add_edge(b - 1, e - 1, c, i);
    }

    dinitz.dinitz(0, n - 1);
    auto ans = dinitz.decompose(0, n - 1);

    cout << ans.size() << "\n";
    for (int i = 0; i < ans.size(); ++i) {
        cout << ans[i].f << " " << ans[i].edg.size() << " ";
        for (auto j : ans[i].edg) {
            cout << j + 1 << " ";
        }

        cout << "\n";
    }

    return 0;
}