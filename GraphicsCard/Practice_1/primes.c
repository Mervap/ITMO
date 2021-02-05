#include <stdio.h>
#include <stdbool.h>
#include <time.h>

long get_time_diff_in_millisec(const struct timespec st, const struct timespec fn) {
  return 1000 * (fn.tv_sec - st.tv_sec) + (fn.tv_nsec - st.tv_nsec) / 1000000;
}

int count(int n) {
  int ans_a = 0;
#pragma omp parallel
  {
    int ans = 0;
#pragma omp for schedule(dynamic, 10) //(static, 1)
    for (int i = 3; i <= n; i += 2) {
      bool f = false;
      for (int j = 2; j * j <= i; ++j) {
        if (i % j == 0) {
          f = true;
          break;
        }
      }
      if (!f) ++ans;
    }
#pragma omp atomic
    ans_a += ans;
  }
  return ans_a;
}

int main() {
  struct timespec st, fn;

  clock_gettime(CLOCK_REALTIME, &st);
  int n = 10000000;
  printf("%d\n", count(n) + 1);
  clock_gettime(CLOCK_REALTIME, &fn);

  printf("Time: %ldms\n", get_time_diff_in_millisec(st, fn));
}
