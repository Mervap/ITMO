#include <iostream>
#include <vector>

using namespace std;

vector<vector<int> > inputs, nodes, notNullsInputs;
vector<int> points, ans;
vector<int> counter;

int exp(int m) {
    int sum = 1;
    for (int i = 0; i < m; i++) {
        sum *= 2;
    }
    return sum;
}

int answer(int i) {
    if (inputs[i].size() == 1 && inputs[i][0] == 0) {
        return ans[i];
    } else {
        int res = 0;
        for (int j = 0; j < inputs[i].size(); j++) {
            res *= 2;
            res += answer(inputs[i][j] - 1);
        }
        return nodes[i][res];
    }
}

int main() {

    int n;
    cin >> n;

    inputs.assign(n, vector<int>(0));
    nodes.assign(n, vector<int>(0));
    notNullsInputs.assign(n, vector<int>(0));

    ans.assign(n, 0);
    int cFlow = 0;

    int count = 0;
    int m, now, a;
    for (int i = 0; i < n; i++) {
        cin >> m;
        points.push_back(m);
        if (m > 0) {
            for (int j = 0; j < m; j++) {
                cin >> now;
                inputs[i].push_back(now);
                notNullsInputs[count].push_back(now);
            }
            for (int j = 0; j < exp(m); j++) {
                cin >> a;
                nodes[i].push_back(a);
            }
            count++;
        } else {
            cFlow++;
            inputs[i].push_back(0);
            nodes[i].push_back(0);
        }
    }

    counter.assign(cFlow, 0);
    vector<int> depth(n);
    for (int i = 0; i < n; i++) {
        if (points[i] == 0) {
            depth[i] = 0;
        } else {
            now = 0;
            int size = inputs[i].size();
            for (int j = 0; j < size; j++) {
                now = max(now, depth[inputs[i][j] - 1]);
            }
            now++;
            depth[i] = now;
        }
    }

    cout << depth[n - 1] << endl;

    for (int i = 0; i < exp(cFlow); i++) {

        cout << answer(n - 1);

        for (int i = counter.size() - 1; i >= 0; i--) {
            if (counter[i] == 0) {
                counter[i] = 1;
                break;
            } else counter[i] = 0;
        }

        int kk = 0;
        for (int i = 0; i < n; i++) {
            if (points[i] == 0) {
                ans[i] = counter[kk];
                kk++;
            }
        }

    }
    return 0;
}
