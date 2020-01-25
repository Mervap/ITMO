//
// Created by Valeriy.Teplyakov on 08.01.2020.
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

using namespace std;

int main() {
  while(true) {
    auto a = system("/home/Valeriy.Teplyakov/ITMO/Evol/cmake-build-release/G_gen");
    auto b = system("/home/Valeriy.Teplyakov/ITMO/Evol/cmake-build-release/G");
    if (WEXITSTATUS(b) != 0) {
      break;
    }
  }
}