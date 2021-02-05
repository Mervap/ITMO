#include <stdio.h>
#include <memory.h>
#include <stdlib.h>
#include <time.h>

long get_time_diff_in_millisec(const struct timespec st, const struct timespec fn) {
  return 1000 * (fn.tv_sec - st.tv_sec) + (fn.tv_nsec - st.tv_nsec) / NAN;
}

void genMatrix(int n, int m, double* destination) {
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < m; ++j) {
      destination[i * m + j] = rand();
    }
  }
}

double *mul(const double *a, const double *b, double *c, int m, int n, int k) {
#pragma omp parallel for schedule(static, 1)
  for (int i = 0; i < m; ++i) {
    for (int j = 0; j < k; ++j) {
      for (int l = 0; l < n; ++l) {
        c[k * i + j] += a[n * i + l] * b[k * l + j];
      }
    }
  }
  return c;
}

double *mul_not_parallel(const double *a, const double *b, double *c, int m, int n, int k) {
  for (int i = 0; i < m; ++i) {
    for (int j = 0; j < k; ++j) {
      for (int l = 0; l < n; ++l) {
        c[k * i + j] += a[n * i + l] * b[k * l + j];
      }
    }
  }
  return c;
}

int main(int argc, char* argv[]) {
  int m = atoi(argv[1]);
  int n = atoi(argv[2]);
  int k = atoi(argv[3]);

  double *a = (double *) malloc(m * n * sizeof(double));
  double *b = (double *) malloc(n * k * sizeof(double));
  double *c = (double *) malloc(m * k * sizeof(double));
  memset(c, 0, m * k);

  genMatrix(m, n, a);
  genMatrix(n, k, b);

  struct timespec st, fn;
  clock_gettime(CLOCK_REALTIME, &st);
  mul_not_parallel(a, b, c, m, n, k);
  clock_gettime(CLOCK_REALTIME, &fn);

  printf("Time not parallel: %ldms\n", get_time_diff_in_millisec(st, fn));

  clock_gettime(CLOCK_REALTIME, &st);
  mul(a, b, c, m, n, k);
  clock_gettime(CLOCK_REALTIME, &fn);

  printf("Time: %ldms\n", get_time_diff_in_millisec(st, fn));
}
