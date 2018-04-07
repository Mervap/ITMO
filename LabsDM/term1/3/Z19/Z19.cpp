
#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main() {
    freopen("num2brackets2.in", "r", stdin);
    freopen("num2brackets2.out", "w", stdout);


    unsigned long long n, k;
    cin >> n >> k;
    ++k;

    vector<vector<unsigned long long> > d;
    d.assign(n * 2 + 1, vector<unsigned long long>(n * 2 + 1));

    d[0][0] = 1;
    for (int i = 0; i <= n * 2; ++i) {
        for (int j = 0; j <= n * 2; ++j) {
            if (i > 0 && j > 0) {
                d[i][j] += d[i - 1][j - 1];
            }
            if (i > 0 && j < n * 2) {
                d[i][j] += d[i - 1][j + 1];
            }
        }
    }

    int cnt = 0;
    vector<char> last(n * 2);
    int l = 0;
    for (int i = n * 2 - 1; i >= 0; --i) {
        //cout << d[i][cnt+1] << " ";
        if (d[i][cnt + 1] * (1ull << (i - cnt - 1) / 2) >= k) {
            last[l] = '(';
            ++l;
            ++cnt;
            cout << "(";
        } else {
            k -= d[i][cnt + 1] * (1ull << (i - cnt - 1) / 2);
            if (l > 0 && last[l - 1] == '(' && d[i][cnt - 1] * (1ull << (i - cnt + 1) / 2) >= k) {
                --l;
                --cnt;
                cout << ")";
            } else {
                if (l > 0 && last[l - 1] == '(') {
                    k -= d[i][cnt - 1] * (1ull << (i - cnt + 1) / 2);
                }
                if (d[i][cnt + 1] * (1ull << (i - cnt - 1) / 2) >= k) {
                    last[l] = '[';
                    ++l;
                    ++cnt;
                    cout << "[";
                } else {
                    k -= d[i][cnt + 1] * (1ull << (i - cnt - 1) / 2);
                    --l;
                    --cnt;
                    cout << "]";
                }
            }
        }
    }

    return 0;
}
