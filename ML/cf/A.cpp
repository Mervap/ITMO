#include <iostream>
#include <unordered_map>
#include <queue>
#include <vector>

using namespace std;

unordered_map<int, vector<int>> classes;
unordered_map<int, int> cur_num;
vector<vector<int>> res;

int main() {
  int m, n, k;
  cin >> n >> m >> k;

  res.resize(k);
  for (int i = 0; i < n; ++i) {
    int c;
    cin >> c;
    classes[c].push_back(i);
  }

  for (int i = 1; i <= m; ++i) {
    int cnt_m = (int) classes[i].size() / k;
    if (cnt_m > 0) {
      auto &cl = classes[i];
      for (int j = 0; j < k; ++j) {
        int num = cur_num[i];
        for (int l = 0; l < cnt_m; ++l) {
          res[j].push_back(cl[num + l]);
        }
        cur_num[i] += cnt_m;
      }
    }
  }

  queue<int> free;
  int min_size = n / k;
  int max_size = min_size + (n % k > 0);

  for (int i = 0; i < k; ++i) {
    auto size = res[i].size();
    if (i < n % k && size < max_size || i >= n % k && size < min_size) {
      free.push(i);
    }
  }

  for (int i = 1; i <= m; ++i) {
    while (true) {
      if (cur_num[i] == classes[i].size()) break;
      auto el = free.front();
      free.pop();
      res[el].push_back(classes[i][cur_num[i]++]);
      auto size = res[el].size();
      if (el < n % k && size < max_size || el >= n % k && size < min_size) {
        free.push(el);
      }
    }
  }

  for (auto &v : res) {
    cout << v.size() << " ";
    for (auto &el : v) {
      cout << el + 1 << " ";
    }
    cout << "\n";
  }

  return 0;
}
