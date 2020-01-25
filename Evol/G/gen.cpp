//
// Created by Valeriy.Teplyakov on 08.01.2020.
//

//
// Created by Valeriy.Teplyakov on 07.01.2020.
//

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
#include <unordered_set>
#include <fstream>
#include <random>

using namespace std;

random_device rd;  //Will be used to obtain a seed for the random number engine
mt19937 gen(rd()); //Standard mersenne_twister_engine seeded with rd()
uniform_int_distribution<> dis(0, 1);
uniform_int_distribution<> dis1(1, 1000);

int main() {
  int n = dis1(gen);
  int fitness = 0;
  vector<int> ans;
  for (int i = 0; i < n; ++i) {
    int nxt = dis(gen);
    ans.push_back(nxt);
    fitness += 1 - nxt;
  }
  freopen("in", "w", stdout);
  cout << n << " " << fitness << "\n";
  for (auto &el : ans) {
    cout << el << " ";
  }
}