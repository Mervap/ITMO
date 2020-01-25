#include <functional>
#include <algorithm>
#include <iostream>
#include <sstream>
#include <complex>
#include <numeric>
#include <cstring>
#include <vector>
#include <string>
#include <cstdio>
#include <queue>
#include <cmath>
#include <map>
#include <set>
#include <fstream>

using namespace std;

#define next			__next
#define sz(a)			int((a).size())
#define FOR(i, a, b)	for (int i(a); i < b; ++i)
#define REP(i, n)		FOR(i, 0, n)
#define CL(a, b)		memset(a, b, sizeof a)
#define pb				push_back

typedef vector <int> vi;

namespace Random {
    unsigned next() {
      static unsigned x = 1987657173, y = 712356789, z = 531288629, w = 138751267;
      unsigned t = x ^ x << 11;
      x = y, y = z, z = w;
      return w = ((t ^ t >> 8) ^ w) ^ w >> 19;
    }
    template <class I> void shuffle(I begin, I end) {
      for (I i = begin; i != end; ++i)
        swap(*i, begin[next() % ((i - begin) + 1)]);
    }
};

#define N	99
#define M	9
#define S	(N * 100)

class bitset {
    unsigned d[(S >> 5) + 1];
    int n;
public:
    void set(int i) {
      int m = i >> 5;
      for (; n <= m; d[n++] = 0);
      d[m] |= 1 << i;
    }
    void operator = (const bitset &b) {
      n = b.n;
      memcpy(d, b.d, n << 2);
    }
    bool operator [] (int i) const {
      return (i >> 5) < n && (d[i >> 5] & 1 << i) != 0;
    }
    void shiftor(const bitset &b, int sh, int m) {
      for (m >>= 5; n <= m; d[n++] = 0);
      unsigned *s = d + (sh >> 5), *e = d + n;
      if (s >= e) return;
      const unsigned *i = b.d;
      if (sh &= 31) {
        unsigned c = 0;
        int rh = 32 - sh;
        e = min(e, s + b.n);
        for (; (e - s) & 3; ++s, c = i++[0])
          s[0] |= (i[0] << sh) | (c >> rh);
        for (; s != e; ) {
          s[0] |= (i[0] << sh) | (c >> rh);
          s[1] |= (i[1] << sh) | (i[0] >> rh);
          s[2] |= (i[2] << sh) | (i[1] >> rh);
          s[3] |= (i[3] << sh) | (i[2] >> rh);
          c = i[3];
          s += 4;
          i += 4;
        }
        s[0] |= c >> rh;
      } else {
        for (; s != e; ++s, ++i)
          s[0] |= i[0];
      }
    }
};

bitset f[N];
int a[N], p[N], row[M], q[M], v[N], *ve = v, n, m;
int first[M], next[N], result_cnt = 0;

bool fit(int k) {
  for (int h = row[k], *i = v, s = 0; i != ve; ++i) {
    ++result_cnt;
    f[(i - v) + 1] = f[(i - v)];
    s = min(s + a[*i], h);
    f[(i - v) + 1].shiftor(f[(i - v)], a[*i], s);
    if (f[(i - v) + 1][h]) {
      for (int *j = i; h; --j)
        if (!f[j - v][h]) {
          p[*j] = k;
          h -= a[*j];
        }
      for (; i >= v; --i)
        if (~p[*i]) {
          next[*i] = first[k];
          first[k] = *i;
          *i = *--ve;
        }
      return true;
    }
  }
  return false;
}

void cancel(int k) {
  for (int i = first[k]; ~i; i = next[i])
    p[*ve++ = i] = -1;
  first[k] = -1;
}

bool solve() {
  if (m == 1) return true;
  --m;
  REP (i, m)
    if (fit(q[i])) {
      for (int j = i; j < m; ++j) swap(q[j], q[j + 1]);
      if (solve()) return ++m, true;
      for (int j = m; j > i; --j) swap(q[j - 1], q[j]);
      cancel(q[i]);
    }
  return ++m, false;
}

#define FAIL exit(0)

ifstream in;

template<typename T> void readOrFail(T &target, char const *message) {
  if (!(in >> target)) {
    printf("PE %s\n", message);
    FAIL;
  }
}

void read() {
  readOrFail(n, "Could not read N");
  readOrFail(m, "Could not read M");
  if (m < 2 || m > 9) {
    printf("PE The number of havens is %d, which is not in [2;9]\n", m);
    FAIL;
  }
  int ship = 0;
  REP (i, m) {
    int count, size = 0;
    readOrFail(count, "Could not read the number of ships in a haven");
    if (count < 1 || count > 100) {
      printf("PE The number of ships in haven #%d is %d, which is not in [1;99]\n", i + 1, count);
      FAIL;
    }
    REP(j, count) {
      if (ship == 99) {
        printf("PE There are more than 99 ships\n");
        FAIL;
      }
      readOrFail(a[ship], "Could not read the ship's length");
      if (a[ship] < 1 || a[ship] > 100) {
        printf("PE Ship #%d in haven #%d has length %d, which is not in [1;100]\n", j + 1, i + 1, a[ship]);
        FAIL;
      }
      p[ship] = -1, *ve++ = ship;
      size += a[ship];
      ++ship;
    }
    row[i] = size;
    q[i] = i;
  }
}

int main(int argc, char *argv[]) {
  in = ifstream(argv[1]);
  auto st = clock();
  f[0].set(0);
  CL(first, -1);
  read();

  while (true) {
    Random::shuffle(a, a + n);
    Random::shuffle(q, q + m);
    if (solve()) break;
  }
  fit(*q);
  REP (i, m) {
    vi ships;
    for (int j = first[i]; ~j; j = next[j])
      ships.pb(a[j]);
//    printf("%d\n", sz(ships));
    REP (j, sz(ships)) {
//      if (j) putchar(' ');
//      printf("%d", ships[j]);
    }
//    puts("");
  }
  auto fn = clock();
  auto out = ofstream(argv[1]);
  out << result_cnt << " " << (fn - st) * 1000 / CLOCKS_PER_SEC << " " << (fn - st);
  return 0;
}