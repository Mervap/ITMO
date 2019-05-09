#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <ctime>

using namespace std;

int n, m;
string a, b;
uint64_t hashA[2001], hashB[2001], p[2001];

void calcHash(string &a, uint64_t *hash) {
    hash[0] = static_cast<uint64_t>(a[0]);
    for (int i = 1; i < a.length(); ++i) {
        hash[i] = hash[i - 1] * 239ull + a[i];
    }
}

uint64_t getSubstrHash(uint64_t *hash, int start, int len) {
    if (len == 0) {
        return 0;
    }

    uint64_t delta = start != 0 ? hash[start - 1] * p[len] : 0;
    return hash[start + len - 1] - delta;
}

bool check(int lenA, int lenB, const vector<int> &entries) {

    int prevA = 0, prevB = 0;
    uint64_t hash = getSubstrHash(hashB, entries[0], lenB);
    for (int e : entries) {
        int commonLen = e - prevA;
        if (getSubstrHash(hashA, prevA, commonLen) != getSubstrHash(hashB, prevB, commonLen)) {
            return false;
        }

        if (getSubstrHash(hashB, prevB + commonLen, lenB) != hash) {
            return false;
        }
        prevA += commonLen + lenA;
        prevB += commonLen + lenB;
    }

    return getSubstrHash(hashA, prevA, n - prevA) == getSubstrHash(hashB, prevB, m - prevB);
}

int main() {
    freopen("curiosity.in", "r", stdin);
    freopen("curiosity.out", "w", stdout);

    cin.tie(0);
    cout.tie(0);
    ios_base::sync_with_stdio(false);

    getline(cin, a);
    getline(cin, b);
    n = a.length();
    m = b.length();

    calcHash(a, hashA);
    calcHash(b, hashB);

    p[0] = 1;
    for (int i = 1; i <= max(n, m); ++i) {
        p[i] = p[i - 1] * 239ull;
    }

    vector<int> ns;
    for(int i = 1; i <= n; ++i) {
        ns.push_back(i);
    }

    random_shuffle(ns.begin(), ns.end());

    int lenA = n, lenB = m, pos = 0;
    for (int i = 1; i <= n; ++i) {
        vector<pair<uint64_t, int>> positions;
        positions.reserve(n - i + 1);
        for (int j = 0; j < n - i + 1; ++j) {
            positions.emplace_back(getSubstrHash(hashA, j, i), j);
        }
        sort(positions.begin(), positions.end());

        int j = 0, k = 0;
        while (j < positions.size()) {
            vector<int> entries;

            int next = -1;
            k = j;
            while (k < positions.size() && positions[j].first == positions[k].first) {
                if (positions[k].second >= next) {
                    entries.push_back(positions[k].second);
                    next = positions[k].second + i;
                }
                ++k;
            }
            j = k;

            int cnt = static_cast<int>(entries.size());
            int rest = n - i * cnt;
            if (rest > m || (m - rest) % cnt != 0) {
                continue;
            }

            int len = (m - rest) / cnt;
            if (i + len < lenA + lenB && check(i, len, entries)) {
                lenA = i;
                lenB = len;
                pos = entries[0];
            }
        }
    }

    cout << "s/" << a.substr(pos, lenA) << "/" << b.substr(pos, lenB) << "/g";
    return 0;
}