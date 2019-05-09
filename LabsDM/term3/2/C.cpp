#include <iostream>
#include <vector>
#include <set>
#include <fstream>
#include <functional>
#include <cmath>
#include <algorithm>
#include <cassert>

using namespace std;
int n, m, s, t, is, it;

struct edge {
    int from;
    int to;
    int n;

    edge(int from, int to, int n) : from(from), to(to), n(n) {};
};

struct finalEdge {
    int to;
    long long w;

    bool operator<(finalEdge const &other) const {
        return (to < other.to);
    }
};

struct finalNode {
    int v;
    long long p;

    bool operator<(finalNode const &other) const {
        return (p < other.p) || (p == other.p && v < other.v);
    }
};

struct node {
    int x, y;

};


vector<node> nodes;

function<bool(edge, edge)> angle(int c) {
    return [c](edge a, edge b) -> bool {
        return atan2(nodes[a.to].y - nodes[c].y, nodes[a.to].x - nodes[c].x) <
               atan2(nodes[b.to].y - nodes[c].y, nodes[b.to].x - nodes[c].x);
    };
}

vector<vector<edge>> edg;
vector<pair<long long, int>> weigthEdg;
vector<vector<int>> was;

vector<set<finalEdge>> finalEdg;
vector<long long> d;
set<finalNode> q;

vector<vector<int>> faces;
vector<int> used;

int main() {
    ios_base::sync_with_stdio(false);
    cin >> n >> m >> s >> t;
    --s, --t;
    int x, y;
    for (int i = 0; i < n; ++i) {
        cin >> x >> y;
        nodes.push_back({x, y});
    }

    int b, e, w;
    edg.resize(n);
    for (int i = 0; i < m; ++i) {
        cin >> b >> e >> w;
        --b, --e;
        edg[b].push_back({b, e, i});
        edg[e].push_back({e, b, i});
        weigthEdg.emplace_back(w, i);
    }

    for (int i = 0; i < n; ++i) {
        sort(edg[i].begin(), edg[i].end(), angle(i));
    }

    faces.resize(m);
    was.resize(n);
    for (int i = 0; i < n; ++i) {
        was[i].resize(edg[i].size());
    }

    int max_face = 0;
    bool sf, tf, f = false;
    for (int i = 0; i < edg.size(); ++i) {
        for (int j = 0; j < edg[i].size(); ++j) {
            if (!was[i][j]) {
                was[i][j] = true;
                sf = false;
                tf = false;
                int b = i;
                auto e = edg[i][j];
                vector<edge> face;
                while (true) {
                    if (!f && b == s) {
                        sf = true;
                    }

                    if (!f && b == t) {
                        tf = true;
                    }

                    face.push_back(e);
                    auto it = upper_bound(edg[e.to].begin(), edg[e.to].end(), edge(0, b, 0), angle(e.to));
                    if (it == edg[e.to].end()) {
                        it = edg[e.to].begin();
                    }

                    if (was[e.to][it - edg[e.to].begin()]) {
                        break;
                    }
                    was[e.to][it - edg[e.to].begin()] = true;
                    b = e.to;
                    e = *it;
                }

                if (sf && tf) {
                    f = true;
                    int i = 0;
                    while (face[i].from != s) {
                        ++i;
                    }

                    while (face[i].from != t) {
                        faces[face[i].n].push_back(max_face);
                        i = (i + 1) % face.size();
                    }

                    is = max_face;

                    ++max_face;
                    while (face[i].from != s) {
                        faces[face[i].n].push_back(max_face);
                        i = (i + 1) % face.size();
                    }

                    it = max_face;
                } else {
                    for (auto e : face) {
                        faces[e.n].push_back(max_face);
                    }
                }
                ++max_face;
            }
        }
    }

    used.resize(n, -1);

    finalEdg.resize(max_face);
    sort(weigthEdg.begin(), weigthEdg.end());

    for (int i = 0; i < m; ++i) {
        auto ed = faces[weigthEdg[i].second];

        assert(ed.size() == 2);
        finalEdg[ed[0]].insert({ed[1], weigthEdg[i].first});
        finalEdg[ed[1]].insert({ed[0], weigthEdg[i].first});
    }


    d.assign(max_face, 100000000000000000);
    d[is] = 0;
    for (int i = 0; i < max_face; ++i) {
        q.insert({i, d[i]});
    }

    while (!q.empty()) {
        auto v = (*q.begin());
        q.erase(q.begin());
        for (auto e : finalEdg[v.v]) {
            if (d[e.to] > d[v.v] + e.w) {
                q.erase({e.to, d[e.to]});
                d[e.to] = d[v.v] + e.w;
                q.insert({e.to, d[e.to]});
            }
        }
    }

    /*cerr << is << " " << it << "\n";
    for (int i = 0; i < max_face; ++i) {
        cerr << d[i] << " ";
    }
    cerr.flush();*/

    cout << d[it];
    return 0;
}