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
#include <random>

using namespace std;

static const int K = 23;
static const int R = K - 10;

random_device rd;
mt19937 gen(rd());
uniform_int_distribution<> dis(0, 1);

int n, last_iter;

unordered_set<int> indices;
int last_fitness;
int compute_new_fitness(const vector<int> &flipped) {
  unordered_set<int> new_indices;
  for (auto &el : flipped) {
    if (indices.count(el) != 0) {
      new_indices.insert(el);
      indices.erase(el);
    }
    else {
      new_indices.insert(el);
      indices.insert(el);
    }
  }

  if (indices.empty()) {
    indices = move(new_indices);
    return last_fitness;
  }
  for (auto &el : indices) cout << el + 1 << " ";
  cout << endl;
  indices = move(new_indices);
  int fitness;
  cin >> fitness;
  if (fitness == n)exit(0);

  last_fitness = fitness;
  return fitness;
}

void safe_block() {
  indices.clear();
}

static vector<int> nxt;
bool variants[R][K];
int fitness_v[R];
bool stupid_chooser(int step, int size, int iter) {
  for (int i : fitness_v) {
    if (i > size - step || i < 0) return false;
  }

  if (size == step) {
    return true;
  }
  auto next_bit = dis(gen);
  for (int i = 0; i < R; ++i) {
    if (variants[i][step] == next_bit) --fitness_v[i];
  }
  if (next_bit) nxt.push_back(iter * K + step);
  if (stupid_chooser(step + 1, size, iter)) return true;
  if (next_bit) {
    nxt.pop_back();
  }
  else {
    nxt.push_back(iter * K + step);
  }
  for (int i = 0; i < R; ++i) {
    if (variants[i][step] != next_bit) {
      --fitness_v[i];
    }
    else {
      ++fitness_v[i];
    }
  }
  if (stupid_chooser(step + 1, size, iter)) return true;
  if (!next_bit) nxt.pop_back();
  for (int i = 0; i < R; ++i) {
    if (variants[i][step] != next_bit) ++fitness_v[i];
  }
  return false;
}

int computeBlock(int iter, int fitness) {
  int size = K;
  if (iter == last_iter - 1 && n % K != 0) {
    size = n % K;
  }

  nxt.clear();
  for (int i = 0; i < size; ++i) {
    nxt.push_back(iter * K + i);
  }
  int fitness_add = compute_new_fitness(nxt);
  int fitness_block_add = (fitness + fitness_add - size) / 2;

  int new_fitness;
  while(true) {
    for (int i = 0; i < R; ++i) {
      nxt.clear();
      for (int j = 0; j < size; ++j) {
        if (dis(gen)) {
          nxt.push_back(K * iter + j);
          variants[i][j] = true;
        } else {
          variants[i][j] = false;
        }
      }
      fitness_v[i] = compute_new_fitness(nxt) - fitness_block_add;
    }

    nxt.clear();
    if (!stupid_chooser(0, size, iter)) { exit(1); }
    new_fitness = compute_new_fitness(nxt);
    if (new_fitness == fitness_block_add + size) break;
  }
  safe_block();
  return new_fitness;
}

int main() {
  ios_base::sync_with_stdio(false);
  cin.tie(nullptr);
  cout.tie(nullptr);

  int fitness;
  cin >> n >> fitness;
  if (fitness == n) exit(0);
  last_fitness = fitness;

  last_iter = n / K + (n % K != 0);
  for (int i = 0; i < last_iter; ++i) {
    fitness = computeBlock(i, fitness);
  }
  exit(1);
}