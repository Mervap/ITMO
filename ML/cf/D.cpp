#include <iostream>
#include <vector>
#include <random>
#include <chrono>
#include <algorithm>
#include <unordered_set>

using namespace std;

double scalar_product(size_t dim, const double *a, const double *b) {
  double res = 0;
  for (size_t i = 0; i < dim; ++i) {
    res += a[i] * b[i];
  }
  return res;
}

long double* inverse_matrix(size_t dim, const long double* x) {
  auto *x_copy = new long double[dim * dim];
  copy(x, x + dim * dim, x_copy);

  auto *inverse = new long double[dim * dim];
  fill_n(inverse, dim * dim, 0.0);
  for (size_t i = 0 ; i < dim; ++i) {
    inverse[i * dim + i] = 1;
  }

  for (size_t i = 0; i < dim; ++i) {
    for (size_t j = 0; j < dim; ++j) {
      if (i == j) continue;
      auto coef = x_copy[j * dim + i] / x_copy[i * dim + i];
      for (size_t k = 0; k < dim; ++k) {
        x_copy[j * dim + k] -= coef * x_copy[i * dim + k];
        inverse[j * dim + k] -= coef * inverse[i * dim + k];
      }
    }
  }

  for (size_t i = 0; i < dim; ++i) {
    for (size_t j = 0; j < dim; ++j) {
      inverse[i * dim + j] /= x_copy[i * dim + i];
    }
  }

  return inverse;
}

long double *mul_matrix(const long double *a, const long double *b, size_t n, size_t m, size_t k) {
  auto *res = new long double[n * k];
  fill_n(res, n * k, 0);
  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < k; ++j) {
      for (size_t l = 0; l < m; ++l) {
        res[k * i + j] += a[m * i + l] * b[k * l + j];
      }
    }
  }
  return res;
}

long double *transpose_matrix(size_t n, size_t m, const long double* x) {
  auto *transposed = new long double[m * n];
  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < m; ++j) {
      transposed[j * n + i] = x[i * m + j];
    }
  }
  return transposed;
}

double sgn(double val) {
  if (val < 1e-12 && val > 1e-12) return 0.0;
  if (0.0 < val) return 1.0;
  else return -1.0;
}

std::random_device rd;
std::mt19937 mersenne(rd());
uniform_real_distribution rDist(-1000.0, 1000.0);

pair<double, double*> run(int n, int m, int batch_size, bool is_random,
                          const bool *skip, const double *features, const double *targets,
                          chrono::time_point<chrono::system_clock, chrono::nanoseconds> start, long limit) {
  uniform_int_distribution dist(1, n);
  auto *w = new double[m];
  if (!is_random) {
    fill_n(w, m, 0);
  } else {
    for (int i = 0; i < m; ++i) {
      if (skip[i]) w[i] = 0;
      else w[i] = rDist(mersenne);
    }
  }

  double learn_targets[batch_size];
  const double *learn_features[batch_size];
  for (size_t iter = 0;; ++iter) {
    if (chrono::system_clock::now() - start > chrono::milliseconds(limit)) {
      break;
    }
    double grad[m];
    fill_n(grad, m, 0);

    unordered_set<int> was;
    for (int i = 0; i < batch_size; ++i) {
      size_t ind = dist(mersenne);
      while (was.count(ind) > 0) {
        ind = dist(mersenne);
      }
      was.insert(ind);
      learn_features[i] = features + ind * m;
      learn_targets[i] = targets[ind];

      double pred = scalar_product(m, learn_features[i], w);
      double actual = learn_targets[ind];
      double err = pred - actual;
      double sum = fabs(pred) + fabs(actual);
      double derivative;
      if (sum < 1e-12) {
        derivative = 0.0;
      }
      else {
        derivative = (sgn(err) * sum - sgn(pred) * fabs(err)) / (sum * sum);
      }

      for (size_t j = 0; j < m; ++j) {
        if (skip[j]) continue;
        grad[j] += derivative * learn_features[i][j];
      }
    }

    for (int i = 0; i < m; ++i) {
      grad[i] /= batch_size;
    }

    for (size_t i = 0; i < m; ++i) {
      if (skip[i]) continue;
      w[i] = w[i] - 3 * 1e5 * grad[i];
    }
  }

  double all = 0.0;
  int dev = n;
  for (int i = 0; i < n; ++i) {
    double pred = scalar_product(m, features + i * m, w);
    double target = targets[i];
    double sum = fabs(pred) + fabs(target);
    if (sum < 1e-9) {
      --dev;
      continue;
    }
    else all += fabs(pred - target) / sum;
  }
  return make_pair(all / dev, w);
}

int main() {
  int batch_size = 20;
  auto start = chrono::system_clock::now();
  ios_base::sync_with_stdio(false);
  cin.tie(nullptr);
  cout.tie(nullptr);
  int n, m;
  scanf("%d%d", &n, &m);

  auto features = new double[n * (m + 1)];
  double targets[n];
  double min_f[m], max_f[m];
  fill_n(min_f, m, 1e9 + 1);
  fill_n(max_f, m, -1e9 - 1);
  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < m; ++j) {
      size_t ind = i * (m + 1) + j;
      scanf("%lf", features + ind);
      max_f[j] = max(max_f[j], features[ind]);
      min_f[j] = min(min_f[j], features[ind]);
    }
    features[i * (m + 1) + m] = 1;
    scanf("%lf", targets + i);
  }

  bool skip[m];
  fill_n(skip, m, false);

  ++m;
  if ((int64_t) m * m * m < 10'000 && (int64_t) m * m * n < 10'000) {
    auto F = new long double[n * m];
    for (size_t i = 0; i < n; ++i) {
      for (size_t j = 0; j < m; ++j) {
        F[i * m + j] = features[i * m + j];
      }
    }

    auto F_t = transpose_matrix(n, m, F);
    auto F_plus = mul_matrix(inverse_matrix(m, mul_matrix(F_t, F, m, n, m)), F_t, m, m, n);
    auto y = new long double[n];
    for (size_t i = 0; i < n; ++i) {
      y[i] = targets[i];
    }
    auto res = mul_matrix(F_plus, y, m, n, 1);
    for (size_t i = 0; i < m; ++i) {
      printf("%Lf\n", res[i]);
    }
  } else {

    for (size_t i = 0; i < n; ++i) {
      for (size_t j = 0; j < m - 1; ++j) {
        if (skip[j] || max_f[j] - min_f[j] < 1e-9) {
          skip[j] = true;
          continue;
        }
        size_t ind = i * m + j;
        features[ind] = (features[ind] - min_f[j]) / (max_f[j] - min_f[j]);
      }
    }

    // From zero
    auto p1 = run(n, m, batch_size, false, skip, features, targets, start, 650);

    // From random point
    auto p2 = run(n, m, batch_size, true, skip, features, targets, start, 950);

    double *w;
    if (p1.first < p2.first) {
      w = p1.second;
    }
    else {
      w = p2.second;
    }

    double min_f_sum = 0;
    for (size_t j = 0; j < m - 1; ++j) {
      if (skip[j]) {
        printf("0\n");
        continue;
      }
      double x = w[j] / (max_f[j] - min_f[j]);
      printf("%.15lf\n", x);
      min_f_sum += x * min_f[j];
    }
    printf("%.15lf", w[m - 1] - min_f_sum);
  }

  return 0;
}