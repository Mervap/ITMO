#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#include <queue>
#include <iterator>

using namespace std;

int main() {
    //freopen("nextsetpartition.in", "r", stdin);
    //freopen("nextsetpartition.out", "w", stdout);

    int n, k;
    cin.sync();

    while (cin >> n >> k && n != 0) {
        char c;
        string s;
        vector<int> pmn;
        vector<vector<int> > mn;

        cin.get(c);
        while (cin.get(c)) {
            if (c == ' ') {
                pmn.push_back(atoi(s.c_str()));
                s = "";
                continue;
            }
            if (c == '\n') {
                pmn.push_back(atoi(s.c_str()));
                s = "";
                mn.push_back(pmn);
                pmn.clear();
                if ((int) mn.size() == k) {
                    break;
                }
                continue;
            }
            s += c;
        }

        vector<int> q;
        int i = (int) mn.size() - 1;
        bool f = true;


        while (i >= 0 && f) {
            int l = (int) mn[i].size() - 1;
            if (!q.empty() && q[(int) q.size() - 1] > mn[i][l]) {
                int t = (int) q.size() - 1;
                while (t != 0 && q[t - 1] > mn[i][l]) {
                    --t;
                }
                mn[i].push_back(q[t]);
                q.erase(q.begin() + t);
                f = false;
            } else {
                for (int j = l; j > 0; --j) {
                    if (!q.empty() && q[(int) q.size() - 1] > mn[i][j]) {
                        int z = mn[i][j];
                        int t = (int) q.size() - 1;
                        while (t != 0 && q[t - 1] > mn[i][j]) {
                            --t;
                        }
                        mn[i][j] = q[t];
                        q.erase(q.begin() + t);
                        q.push_back(z);
                        sort(q.begin(), q.end());
                        f = false;
                        break;
                    }
                    q.push_back(mn[i][j]);
                    mn[i].erase(mn[i].begin() + j);
                    sort(q.begin(), q.end());
                }
                if (f) {
                    q.push_back(mn[i][0]);
                    mn.erase(mn.begin() + i);
                    sort(q.begin(), q.end());
                }
            }
            --i;
        }

        cout << n << " " << (int) mn.size() + q.size() << "\n";
        for (int i = 0; i < (int) mn.size(); ++i) {
            for (int j = 0; j < (int) mn[i].size(); ++j) {
                cout << mn[i][j] << " ";
            }
            cout << "\n";
        }

        for (int i = 0; i < (int) q.size(); ++i) {
            cout << q[i] << " \n";
        }
        cout << "\n";

    }
    return 0;
}
