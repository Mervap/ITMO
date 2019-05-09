#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

const int64_t MOD = 104'857'601;

int64_t getReal(int ind, vector<int64_t> &v) {
    if (ind >= v.size()) {
        return 0;
    }

    return v[ind];
}

vector<int64_t> mulPol(vector<int64_t> &p, vector<int64_t> &q) {
    int n = p.size();
    int m = q.size();

    int size = n + m + 3;
    vector<int64_t> mul(size);
    for (int i = 0; i < size; i += 2) {
        for (int j = 0; j <= i; ++j) {
            mul[i] += (getReal(j, p) * getReal(i - j, q)) % MOD;
            mul[i] %= MOD;
        }
    }

    return mul;
}

int main() {
    int64_t n, k;
    cin >> k >> n;
    --n;

    vector<int64_t> p(2 * k), q(k + 1);

    for (int i = 0; i < k; ++i) {
        cin >> p[i];
    }

    q[0] = 1;
    int64_t qq;
    for (int i = 1; i <= k; i++) {
        cin >> qq;
        q[i] = (-qq + MOD) % MOD;
    }


    while (n >= k) {
        for (int i = k; i < 2 * k; ++i) {
            p[i] = 0;
            for (int j = 1; j <= k; ++j) {
                p[i] -= (q[j] * p[i - j]) % MOD;
                p[i] %= MOD;

                if (p[i] < 0) {
                    p[i] += MOD;
                }
            }
        }

        vector<int64_t> minusQ(k + 1);
        for (int i = 0; i <= k; i += 2) {
            minusQ[i] = q[i];
        }

        for (int i = 1; i <= k; i += 2) {
            minusQ[i] = (-q[i] + MOD) % MOD;
        }

        auto r = mulPol(q, minusQ);
        for (int i = 0; i <= k; ++i) {
            q[i] = r[i * 2];
        }

        for (int i = n % 2; i < 2 * k; i += 2) {
            p[i / 2] = p[i];
        }

        n = n / 2;
    }

    cout << p[n];
}