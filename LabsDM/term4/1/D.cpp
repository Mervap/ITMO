#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

vector<vector<vector<int>>> listPart, setPart;

vector<vector<int>> getSetTerms(int n, int min) {
    vector<vector<int>> ans;
    for (int j = min; j <= n; ++j) {
        for (int k = 1; k <= n / j; ++k) {
            auto tmp = getSetTerms(n - j * k, j + 1);
            for (auto &e : tmp) {
                for (int kk = 1; kk <= k; ++kk) {
                    e.push_back(j);
                }
                ans.push_back(std::move(e));
            }

            if (k * j == n) {
                vector<int> tmpp;
                for (int kk = 0; kk < k; ++kk) {
                    tmpp.push_back(j);
                }
                ans.push_back(std::move(tmpp));
            }
        }
    }

    return ans;
}

vector<vector<int>> getListTerms(int n) {
    vector<vector<int>> ans;
    for (int j = 1; j < n; ++j) {
        auto tmp = getListTerms(n - j);
        for (auto &e : tmp) {
            e.push_back(j);
            ans.push_back(std::move(e));
        }
    }

    if (n > 0) {
        ans.push_back({n});
    }

    return ans;
}

void init() {
    for (int i = 0; i < 7; ++i) {
        listPart.push_back(std::move(getListTerms(i)));
        setPart.push_back(std::move(getSetTerms(i, 1)));
    }
}

uint64_t binCoef(uint64_t n, uint64_t k) {

    n = n + k - 1;

    if (k == 0) {
        return 1;
    }

    if (n < k) {
        return 0;
    }

    uint64_t ans = 1;
    for (uint64_t i = n - k + 1; i <= n; ++i) {
        ans *= i;
    }

    for (uint64_t i = 2; i <= k; ++i) {
        ans /= i;
    }

    return ans;
}

vector<uint64_t> getObject() {

    char c, braceOfComma;
    cin >> c;

    if (c == 'B') {
        return {0, 1, 0, 0, 0, 0, 0};
    }

    cin >> braceOfComma;
    vector<uint64_t> l = getObject();
    cin >> braceOfComma;


    vector<uint64_t> ans(7);

    if (c == 'L') {
        ans[0] = 1;
        for (int i = 1; i < 7; ++i) {
            for (auto &v : listPart[i]) {
                uint64_t tmp = 1;
                for (auto e : v) {
                    tmp *= l[e];
                }
                ans[i] += tmp;
            }
        }
    }

    if (c == 'S') {
        ans[0] = 1;
        for (int i = 1; i < 7; ++i) {
            for (auto &v : setPart[i]) {
                uint64_t tmp = 1;
                vector<int> was(7);
                for (auto e : v) {
                    ++was[e];
                }

                for (int j = 1; j < 7; ++j) {
                    tmp *= binCoef(l[j], was[j]);
                }
                ans[i] += tmp;
            }
        }
    }

    if (c == 'P') {
        vector<uint64_t> r = getObject();
        cin >> braceOfComma;

        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j <= i; ++j) {
                ans[i] += l[j] * r[i - j];
            }
        }
    }

    return ans;
}

int main() {

    init();

    auto ans = getObject();

    for (auto e : ans) {
        cout << e << " ";
    }

    return 0;
}