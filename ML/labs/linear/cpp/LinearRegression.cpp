#include <iostream>
#include <vector>
#include <random>
#include <chrono>

using namespace std;

double scalar_product(size_t dim, const double *a, const double *b) {
  double res = 0;
  for (size_t i = 0; i < dim; ++i) {
    res += a[i] * b[i];
  }
  return res;
}

double find_learning_rate(size_t m, const double *grad, const double *features, double target, const double *w, double reg) {
  static const double golden_ratio = (1 + sqrt(5)) / 2;
  auto error = [&](double learning_rate) {
    double new_w[m];
    for (int i = 0; i < m; ++i) {
      new_w[i] = (1 - learning_rate * reg) * w[i] - learning_rate * grad[i];
    }
    double diff = scalar_product(m, features, new_w) - target;
    return diff * diff;
  };
  double l = 0, r = 1, e = 1e-5;
  while (fabs(r - l) > e) {
    double len = r - l;
    double x1 = r - len / golden_ratio;
    double x2 = l + len / golden_ratio;
    if (error(x1) >= error(x2)) {
      l = x1;
    } else {
      r = x2;
    }
  }
  return (l + r) / 2;
}

int main() {
  ios_base::sync_with_stdio(false);
  cin.tie(nullptr);
  cout.tie(nullptr);
  int filenum, iter_max;
  double reg;
  scanf("%d%d%lf", &filenum, &iter_max, &reg);

  string file_name = "data/" + to_string(filenum) + ".txt";
  freopen(file_name.c_str(), "r", stdin);
  int n, m;
  scanf("%d%d", &m, &n);

  auto features = new double[n * (m + 1)];
  double targets[n], min_f[m], max_f[m];
  double max_train_y = -1e9 - 1, min_train_y = 1e9 + 1;
  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < m; ++j) {
      size_t ind = i * (m + 1) + j;
      scanf("%lf", features + ind);
      max_f[j] = max(max_f[j], features[ind]);
      min_f[j] = min(min_f[j], features[ind]);
    }
    features[i * (m + 1) + m] = 1;
    scanf("%lf", targets + i);
    max_train_y = max(max_train_y, targets[i]);
    min_train_y = min(min_train_y, targets[i]);
  }

  int test_n;
  scanf("%d", &test_n);
  auto test_features = new double[test_n * (m + 1)];
  double max_test_y = -1e9 - 1, min_test_y = 1e9 + 1;
  double test_targets[test_n];

  for (size_t i = 0; i < test_n; ++i) {
    for (size_t j = 0; j < m; ++j) {
      scanf("%lf", test_features + j);
    }
    test_features[m] = 1;
    scanf("%lf", test_targets + i);
    max_test_y = max(max_test_y, test_targets[i]);
    min_test_y = min(min_test_y, test_targets[i]);
  }

  ++m;
  for (size_t i = 0; i < n; ++i) {
    for (size_t j = 0; j < m - 1; ++j) {
      size_t ind = i * m + j;
      features[ind] = (features[ind] - min_f[j]) / (max_f[j] - min_f[j]);
    }
  }

  double w[m], grad[m];
  fill_n(w, m, 0);
  fill_n(grad, m, 0);

  std::random_device rd;
  std::mt19937 mersenne(rd());

  for (size_t iter = 0; iter < iter_max; ++iter) {
    size_t ind = mersenne() % n;

    double derivative = 2 * (scalar_product(m, features + ind * m, w) - targets[ind]);
    for (size_t j = 0; j < m; ++j) {
      grad[j] = derivative * features[ind * m + j];
    }

    double learning_rate = find_learning_rate(m, grad, features + ind * m, targets[ind], w, reg);
    for (size_t i = 0; i < m; ++i) {
      w[i] = (1 - learning_rate * reg) * w[i] - learning_rate * grad[i];
    }

    // Train

    double min_f_sum = 0;
    double test_w[m];
    for (size_t j = 0; j < m - 1; ++j) {
      test_w[j] = w[j] / (max_f[j] - min_f[j]);
      min_f_sum += test_w[j] * min_f[j];
    }
    test_w[m - 1] = w[m - 1] - min_f_sum;

    double nrmse = 0;
    for (size_t i = 0; i < n; ++i) {
      auto predicted = scalar_product(m, features + (i * m), test_w);
      auto diff = (predicted - targets[i]) / (max_train_y - min_train_y);
      nrmse += diff * diff / n;
    }
    nrmse = sqrt(nrmse);
    printf("%lf ", nrmse);

    // Test
    nrmse = 0;
    for (size_t i = 0; i < test_n; ++i) {
      auto predicted = scalar_product(m, test_features + (i * m), test_w);
      auto diff = (predicted - test_targets[i]) / (max_test_y - min_test_y);
      nrmse += diff * diff / test_n;
    }

    nrmse = sqrt(nrmse);
    printf("%lf\n", nrmse);
  }

  return 0;
}