#include <iostream>
#include <complex>
#include <string>
#include <cstdio>
#include <cmath>
#include <fstream>
#include <zconf.h>
#include <cstring>
#include <random>
#include <algorithm>
#include <future>
#include "solution.h"

using namespace std;

namespace Random {
    random_device rd;  //Will be used to obtain a seed for the random number engine
    mt19937 gen(rd()); //Standard mersenne_twister_engine seeded with rd()
    uniform_int_distribution<> havens_dis(1, M);
    uniform_int_distribution<> havens_diff_dis(1, 30);
    uniform_int_distribution<> count_ship_dis(1, N);
    uniform_int_distribution<> ship_size_dis(1, 100);

    int next_havens_diff() {
      auto val = havens_diff_dis(gen);
      if (val == 1) {
        return -1;
      } else if (val == 30) {
        return 1;
      } else {
        return 0;
      }
    }

    int next_count_ship_diff() {
      auto val = count_ship_dis(gen);
      if (val < 30) {
        return -1;
      } else if (val > 70) {
        return 1;
      } else {
        return 0;
      }
    }

    int next_ship_size_diff() {
      auto val = ship_size_dis(gen);
      if (val == 1) {
        return -1;
      } else if (val == 99) {
        return 1;
      } else {
        return 0;
      }
    }
}


int cntt = 0;

struct TestCase {

    TestCase() {
      if (access(best_test_file_name, F_OK) != -1) {
        auto best_test_file = ifstream(best_test_file_name);
        best_test_file >> sum_ships >> haven_counts;
        for (int i = 0; i < haven_counts; ++i) {
          best_test_file >> ship_count[i];
          for (int j = 0; j < ship_count[i]; ++j) {
            best_test_file >> ship_sizes[i][j];
          }
          for (int j = ship_count[i]; j < N; ++j) {
            ship_sizes[i][j] = Random::ship_size_dis(Random::gen);
          }
        }
        for (int i = haven_counts; i < M; ++i) {
          ship_count[i] = 10;
          for (int j = 0; j < 10; ++j) {
            ship_sizes[i][j] = Random::ship_size_dis(Random::gen);
          }
        }
        best_test_file.close();
      } else {
        sum_ships = 54;
        haven_counts = 9;
        for (int i = 0; i < M; ++i) {
          ship_count[i] = 6;
          for (int j = 0; j < N; ++j) {
            ship_sizes[i][j] = Random::ship_size_dis(Random::gen);
          }
        }
      }
    }

    TestCase(const TestCase &other) {
      haven_counts = other.haven_counts;
      sum_ships = other.sum_ships;
      fitness = other.fitness;
      time = other.time;
      memcpy(ship_count, other.ship_count, M * sizeof(int));
      memcpy(ship_sizes, other.ship_sizes, M * N * sizeof(int));
    }

    void mutate() {
/*      auto old_haven = haven_counts;
      haven_counts += Random::next_havens_diff();
      haven_counts = min(max(haven_counts, 2), M);

      auto cur_sum = 0;
      for (int i = 0; i < haven_counts; ++i) {
        cur_sum += ship_count[i];
      }*/

      auto cur_sum = sum_ships;
      sum_ships += Random::next_count_ship_diff();
      sum_ships = min(max(sum_ships, 1), N);

      auto diff = sum_ships - cur_sum;
      for (int i = 0; i < abs(diff); ++i) {
        auto ind = Random::havens_dis(Random::gen) % haven_counts;
        if (diff > 0) {
          ++ship_count[ind];
          ship_sizes[ind][ship_count[ind] - 1] = Random::ship_size_dis(Random::gen);
        } else {
          while (ship_count[ind] == 1) {
            ind = Random::havens_dis(Random::gen) % haven_counts;
          }
          --ship_count[ind];
        }
      }

      for (int i = 0; i < 4; ++i) {
        if (Random::havens_dis(Random::gen) < 6) {
          auto from = Random::havens_dis(Random::gen) - 1;
          auto to = Random::havens_dis(Random::gen) - 1;
          if (ship_count[from] > 1 && ship_count[to] < N) {
            --ship_count[from];
            ship_sizes[to][ship_count[to]] = ship_sizes[from][ship_count[from]];
            ++ship_count[to];
          }
        }
      }

      for (int i = 0; i < 5; ++i) {
        if (Random::havens_dis(Random::gen) < 5) {
          auto from = Random::havens_dis(Random::gen) - 1;
          auto to = Random::havens_dis(Random::gen) - 1;

          auto from_ind = (Random::count_ship_dis(Random::gen) - 1) % ship_count[from];
          auto to_ind = (Random::count_ship_dis(Random::gen) - 1) % ship_count[to];
          swap(ship_sizes[from][from_ind], ship_sizes[to][to_ind]);
        }
      }

      for (int i = 0; i < haven_counts; ++i) {
        for (int j = 0; j < ship_count[i]; ++j) {
          ship_sizes[i][j] += Random::next_ship_size_diff();
          ship_sizes[i][j] = min(max(ship_sizes[i][j], 1), 100);
        }
      }
    }

    void recalc_fitness(const string &ii) {
      int ind = 0;
      int all_ships[N * M];
      int cnt[M];
      for (int i = 0; i < haven_counts; ++i) {
        cnt[i] = 0;
        for (int j = 0; j < ship_count[i]; ++j) {
          all_ships[ind++] = ship_sizes[i][j];
          cnt[i] += ship_sizes[i][j];
        }
      }
      int st = clock();

      out_test(ii);
      auto command =
          "/home/Valeriy.Teplyakov/ITMO/Evol/cmake-build-release/D_gen /home/Valeriy.Teplyakov/ITMO/Evol/D/tests/thread_" +
          ii;
      std::system(command.c_str());
      auto res = ifstream("/home/Valeriy.Teplyakov/ITMO/Evol/D/tests/thread_" + ii);
      res >> fitness >> time;
      if (fitness == sum_ships && time == haven_counts) {
        cout << "ERR!";
      }
      res.close();
    }

    void out() {
      ofstream out(best_test_file_name);
      out << sum_ships << " " << haven_counts << "\n";
      for (int i = 0; i < haven_counts; ++i) {
        out << ship_count[i] << "\n";
        for (int j = 0; j < ship_count[i]; ++j) {
          out << ship_sizes[i][j] << " ";
        }
        out << "\n";
      }

      out << "\n\nFitness: " << fitness << "\nTime spent: " << time;
    }

    void backup(int i) {
      ofstream out("/home/Valeriy.Teplyakov/ITMO/Evol/D/backup/" + to_string(i));
      out << sum_ships << " " << haven_counts << "\n";
      for (int i = 0; i < haven_counts; ++i) {
        out << ship_count[i] << "\n";
        for (int j = 0; j < ship_count[i]; ++j) {
          out << ship_sizes[i][j] << " ";
        }
        out << "\n";
      }

      out << "\n\nFitness: " << fitness << "\nTime spent: " << time;
    }

    void out_test(const string &ii) {
      ofstream out("/home/Valeriy.Teplyakov/ITMO/Evol/D/tests/thread_" + ii);
      out << sum_ships << " " << haven_counts << "\n";
      for (int i = 0; i < haven_counts; ++i) {
        out << ship_count[i] << "\n";
        for (int j = 0; j < ship_count[i]; ++j) {
          out << ship_sizes[i][j] << " ";
        }
        out << "\n";
      }
    }

    int fitness = 0;
    long time = -1;
    int ship_sizes[M][N]{}, ship_count[M]{}, haven_counts, sum_ships;
    const char *best_test_file_name = "/home/Valeriy.Teplyakov/ITMO/Evol/D/best_test";
};

bool cmp(const TestCase &a, const TestCase &b) {
  return a.fitness > b.fitness;
}

int main() {
  auto test_case = TestCase();
  test_case.recalc_fitness("0");
  TestCase popul[200];
  popul[0] = test_case;
  for (int i = 1; i < 100; ++i) {
    popul[i] = TestCase(test_case);
    popul[i].mutate();
    popul[i].recalc_fitness("0");
  }

  vector<thread> workers;
  condition_variable condition;
  mutex queue_mutex;
  unique_lock<std::mutex> lock(queue_mutex);

  std::atomic_int th[10];


  auto best_fitness = test_case.fitness;

  for (int i = 0; i < 10; ++i) {
    workers.emplace_back([&popul, &lock, &condition, i, &th, &best_fitness]() {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wmissing-noreturn"
        while (true) {
          while (th[i] == 0) {
            condition.wait(lock);
          }
//          auto st = clock();
          for (int j = 0; j < 10; ++j) {
            auto ind = 100 + i * 10 + j;
            popul[ind] = TestCase(popul[Random::ship_size_dis(Random::gen) % 100]);
            popul[ind].mutate();
            popul[ind].recalc_fitness(to_string(i));
            while (popul[ind].fitness == best_fitness) {
              popul[ind].mutate();
              popul[ind].recalc_fitness(to_string(i));
            }
          }
//          cout << "Thread " << i << " OK" << endl;
          --th[i];
//          cout << "Thread " << i << " spent " << (clock() - st) * 1000 / CLOCKS_PER_SEC << endl;
        }
#pragma clang diagnostic pop
    });
  }
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wmissing-noreturn"
  for (int i = 0;; ++i) {

    auto st = clock();
    if (i % 10 == 0) {
      cout << "Iteration: " << i << endl;
      st = clock();
    }

    for (auto &j : th) {
      j = 1;
    }
    condition.notify_all();
    for (auto &j : th) {
      while (j != 0) {}
    }

//    cout << "Wait finish" << endl;

    sort(popul, popul + 200, cmp);

    if (popul[0].fitness > best_fitness) {
      best_fitness = popul[0].fitness;
      popul[0].out();
    }

    if (i % 10 == 0) {
      cout << "Time: " << (clock() - st) * 1000 / CLOCKS_PER_SEC << endl;
    }

    if (i % 300 == 0) {
      for (int j = 0; j < 200; ++j) {
        popul[j].backup(j);
      }
    }
  }
#pragma clang diagnostic pop
  return 0;
}
