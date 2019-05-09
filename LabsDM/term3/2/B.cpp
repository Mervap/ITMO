#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <fstream>
#include <functional>
#include <cmath>
#include <algorithm>
#include <cassert>

using namespace std;
int n, m, s, t, is, it;
const double eps = 1e-12;

struct node {
    double x, y;

    node(double xx, double yy) {
        x = (xx == 0 ? abs(xx) : xx);
        y = (yy == 0 ? abs(yy) : yy);
    }

    bool operator<(const node &v) const {
        return (x - v.x < -eps) || (abs(x - v.x) < eps && y - v.y < -eps);
    }
};

int max_n = 0;

struct line {
    double a, b, c;

    line(int x1, int y1, int x2, int y2) {
        a = y1 - y2;
        b = x2 - x1;
        c = x1 * y2 - x2 * y1;
    }

    bool isCross(const line &other) {
        return ((a * other.b - other.a * b) > eps) || ((a * other.b - other.a * b) < -eps);
    }

    node cross(const line &other) {
        return node((other.c * b - c * other.b) / (a * other.b - other.a * b),
                    (other.a * c - a * other.c) / (a * other.b - other.a * b));
    }
};

vector<node> nodes;
map<node, int> number;

function<bool(int, int)> angle(int c) {
    return [c](int a, int b) -> bool {
        return atan2(nodes[a].y - nodes[c].y, nodes[a].x - nodes[c].x) <
               atan2(nodes[b].y - nodes[c].y, nodes[b].x - nodes[c].x);
    };
}

vector<vector<int>> edg;
vector<vector<int>> was;
vector<double> squares;

double det(double a1, double a2, double b1, double b2) {
    return a1 * b2 - a2 * b1;
}

int main() {


    cin >> n;
    vector<line> lines;
    int x1, x2, y1, y2;
    for (int i = 0; i < n; ++i) {
        cin >> x1 >> y1 >> x2 >> y2;
        lines.emplace_back(x1, y1, x2, y2);
    }

    for (int i = 0; i < n; ++i) {
        vector<node> tmp;
        for (int j = 0; j < n; ++j) {
            if (lines[i].isCross(lines[j])) {
                node l = lines[i].cross(lines[j]);
                if (!number.count(l)) {
                    nodes.push_back(l);
                    number[l] = max_n++;
                    edg.push_back(vector<int>());
                }
                tmp.push_back(l);
            }
        }

        if (!tmp.empty()) {
            sort(tmp.begin(), tmp.end());
            for (int j = 0; j < tmp.size() - 1; ++j) {
                if (number[tmp[j]] != number[tmp[j + 1]]) {
                    edg[number[tmp[j]]].push_back(number[tmp[j + 1]]);
                    edg[number[tmp[j + 1]]].push_back(number[tmp[j]]);
                }
            }
        }
    }

    for (int i = 0; i < edg.size(); ++i) {
        sort(edg[i].begin(), edg[i].end(), angle(i));
    }

    was.resize(edg.size());
    for (int i = 0; i < edg.size(); ++i) {
        was[i].resize(edg[i].size());
    }

    for (int i = 0; i < max_n; ++i) {
        for (int j = 0; j < edg[i].size(); ++j) {
            if (!was[i][j]) {
                was[i][j] = true;
                int b = i;
                auto e = edg[i][j];
                vector<node> face;
                while (true) {
                    face.push_back(nodes[b]);
                    auto it = upper_bound(edg[e].begin(), edg[e].end(), b, angle(e));
                    if (it == edg[e].end()) {
                        it = edg[e].begin();
                    }

                    if (was[e][it - edg[e].begin()]) {
                        break;
                    }
                    was[e][it - edg[e].begin()] = true;
                    b = e;
                    e = *it;
                }

                double sum = 0;
                for (int k = 0; k < face.size(); ++k) {
                    sum += det(face[k].x, face[k].y, face[(k + 1) % face.size()].x, face[(k + 1) % face.size()].y);
                }
                squares.push_back(abs(sum / 2));
            }
        }
    }

    sort(squares.begin(), squares.end());

    int i = 0;
    double sum = 0;
    if (!squares.empty()) {
        while ((i < squares.size() - 1) && (squares[i] < 1e-8)) {
            sum += squares[i];
            ++i;
        }
        cout << squares.size() - i - 1 << "\n";

        cout.precision(12);
        for (; i < squares.size() - 1; ++i) {
            sum += squares[i];
            cout << squares[i] << "\n";
        }
    } else {
        cout << "0\n";
    }
    return 0;
}