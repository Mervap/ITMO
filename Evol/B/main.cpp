//
// Created by Valeriy.Teplyakov on 18.01.2020.
//

#include <functional>
#include <algorithm>
#include <iostream>
#include <complex>
#include <vector>
#include <string>
#include <queue>
#include <cmath>
#include <map>
#include <set>
#include <random>
#include <cassert>

#define double long double

using namespace std;

const int MAX_DIST = 9980;
const double EPS = 10e-9;
const int CELL_SIZE = 10;
const double DIAG_DIST_CELL = (CELL_SIZE / 2) * sqrt(2);

random_device rd;
mt19937 gen(rd());
uniform_real_distribution<> dis(1, 2);

struct Point {

    Point() : Point(0, 0) {}

    Point(double x, double y) : x(x), y(y) {}

    void shift(Point p0) {
      x += p0.x;
      y += p0.y;
    }

    void rotate(double angle) {
      auto old_x = x;
      auto old_y = y;
      x = old_x * cos(angle) - old_y * sin(angle);
      y = old_x * sin(angle) + old_y * cos(angle);
    }

    double x, y;
};

const Point CENTER = Point();

struct Circle {
    Circle(Point p, double radius) {
      x = p.x;
      y = p.y;
      r = radius;
    }

    double x, y, r;
};

Point shifted(Point new_O, Point p) {
  return {p.x - new_O.x, p.y - new_O.y};
}

double dot_product(Point a, Point b) {
  return a.x * b.x + a.y * b.y;
}

double cross_product(Point a, Point b) {
  return a.x * b.y - a.y * b.x;
}

double dist(Point a, Point b) {
  auto dx = a.x - b.x;
  auto dy = a.y - b.y;
  return sqrt(dx * dx + dy * dy);
}

double get_angle(Point p1, Point p2) {
  return atan2(cross_product(p1, p2), dot_product(p1, p2));
}

pair<Point, Point> intersect_circles(Circle o1, Circle o2) {
  auto dx = o2.x - o1.x;
  auto dy = o2.y - o1.y;
  auto d = sqrt(dx * dx + dy * dy);

  auto a = (o1.r * o1.r - o2.r * o2.r + d * d) / (2 * d);
  auto h = sqrt(o1.r * o1.r - a * a);
  auto c = a / d;
  auto x = o1.x + c * dx;
  auto y = o1.y + c * dy;

  auto c1 = h / d;
  auto p1 = Point(x + c1 * dy, y - c1 * dx);
  auto p2 = Point(x - c1 * dy, y + c1 * dx);
  return {p1, p2};
}

bool is_right(Point *corner) {
  auto p1 = shifted(corner[0], corner[1]);
  auto p2 = shifted(corner[1], corner[2]);
  auto ang = get_angle(p1, p2);
  return abs(ang - M_PI / 2) < EPS;
}

pair<string, double> get_dist(Point p) {
  cout.precision(15);
  cout << "activate " << p.x << " " << p.y << endl;
  string a, b;
  double res = 0.0;

  cin >> a;
  if (a != "blocked") {
    cin >> res;
  }
  cin >> b;
  assert(b == "active");

  return {a, res};
}

pair<Point, Point> find_corners(Point p1, Point p2) {
  auto r1 = get_dist(p1);
  auto r2 = get_dist(p2);
  return intersect_circles(Circle(p1, r1.second), Circle(p2, r2.second));
}

pair<Point, double> find_labyrinth() {
  Point corner[] = {CENTER, Point(1, 0), Point(0, 1)};

  while (!is_right(corner)) {
    auto diff_angle = dis(rd) * M_PI / 2;

    for (int i = 0; i < 3; ++i) {
      auto real_angle = diff_angle + i * M_PI / 2;
      auto p1 = Point(MAX_DIST * cos(real_angle), MAX_DIST * sin(real_angle));
      auto p2 = Point(MAX_DIST * cos(real_angle + 0.01), MAX_DIST * sin(real_angle + 0.01));

      auto probable_corners = find_corners(p1, p2);
      if (dist(probable_corners.first, CENTER) < dist(probable_corners.second, CENTER)) {
        corner[i] = probable_corners.first;
      } else {
        corner[i] = probable_corners.second;
      }
    }
  }

  auto corner_center = corner[1];
  auto OY = Point(0, 1);
  auto angle = get_angle(OY, shifted(corner_center, corner[2]));
  if (angle < 0) {
    angle = get_angle(OY, shifted(corner_center, corner[0]));
  }
  return {corner_center, angle};
}

void search_inside(int n, int m, Point corner_center, double angle) {
  n = max(n, m);
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      auto p = Point(5 + 10 * j, 5 + 10 * i);
      p.rotate(angle);
      p.shift(corner_center);

      auto ans = get_dist(p);
      if (ans.first == "outside" || ans.first == "blocked") continue;
      if (ans.second < DIAG_DIST_CELL) {
        auto corners = find_corners(p, Point(p.x + 2.5, p.y + 2.5));
        auto corner1 = corners.first, corner2 = corners.second;

        auto ans1 = get_dist(corner1);
        auto ans2 = get_dist(corner2);

        Point p_ans;
        if (ans1.first == "inside") {
          if (ans2.first == "inside") {
            if (ans1.second < ans2.second) {
              p_ans = corner1;
            } else {
              p_ans = corner2;
            }
          } else {
            p_ans = corner1;
          }
        } else {
          p_ans = corner2;
        }

        cout << "found " << round(p_ans.x) << " " << round(p_ans.y) << endl;
        return;
      }
    }
  }
}

int main() {
  int n, m;
  cin >> n >> m;
  auto lab_data = find_labyrinth();
  search_inside(n, m, lab_data.first, lab_data.second);
}