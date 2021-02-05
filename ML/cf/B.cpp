#include <iostream>
#include <vector>

using namespace std;

double f(double precision, double recall) {
  if (precision + recall > 0) {
    return 2.0 * precision * recall / (precision + recall);
  }
  else {
    return 0;
  }
}

int main() {
  int k;
  cin >> k;

  vector<int> row_sum(k), column_sum(k), ok(k);
  int total_sum = 0;
  for (int i = 0; i < k; ++i) {
    for (int j = 0; j < k; ++j) {
      int c;
      cin >> c;
      row_sum[i] += c;
      column_sum[j] += c;
      if (i == j) {
        ok[i] = c;
      }
      total_sum += c;
    }
  }

  double f_macro = 0, precision = 0, recall = 0;
  for (int i = 0; i < k; ++i) {
    double local_precision = 0;
    if (row_sum[i] > 0) {
      local_precision = (double) ok[i] / row_sum[i];
    }
    double local_recall = 0;
    if (column_sum[i] > 0) {
      local_recall = (double) ok[i] / column_sum[i];
    }
    precision += local_precision * row_sum[i];
    recall += local_recall * row_sum[i];
    f_macro += f(local_precision, local_recall) * row_sum[i];
  }

  precision = precision / total_sum;
  recall = recall / total_sum;
  f_macro = f_macro / total_sum;
  double f_micro = f(precision, recall);

  cout.precision(8);
  cout << f_micro << "\n"<< f_macro;
  return 0;
}
