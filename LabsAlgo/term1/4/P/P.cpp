//TL 4/70 tests, scores 70 points

#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

#define next nxxt

using namespace std;

typedef int arr[10002];
arr head, next, edg, num, kol, parent;

typedef int arr1[5001][5001];
arr1 dp;
int n, m, l = 0, mx = 0;

int dfs(int i, int p) {
    int h = head[i];
    parent[i] = p;
    if (edg[h] == p) {
        head[i] = next[h];
        h = head[i];
    }
    dp[i][1] = 0;ø

    while (h != -1) {
        kol[i] += dfs(edg[h], i);
        if (edg[next[h]] == p) {
            next[h] = next[next[h]];
        }
        h = next[h];
    }

    h = head[i];
    while (h != -1) {
        int v = edg[h];
        for (int j = kol[i]; j >= 0; --j) {
            dp[i][j] += 1;
            for (int k = min(j, kol[v]); k >= 1; --k) {
                dp[i][j] = min(dp[i][j], dp[i][j - k] + dp[v][k]);
                if (kol[i] == kol[v] + 1 && j == k + 1) {
                    break;
                }
            }
        }
        h = next[h];
    }
    return kol[i];
}

void out(int i, int c) {

    if (c == kol[i]) {
        return;
    }
    // c--;
    int kol_ch = 0;
    int h = head[i];
    while (h != -1) {
        ++kol_ch;
        h = next[h];
    }

    int **ch = new int *[c + 1];
    for (int i1 = 0; i1 < c + 1; ++i1) {
        ch[i1] = new int[kol_ch];
        for (int i2 = 0; i2 < kol_ch; ++i2) {
            ch[i1][i2] = 0;
        }
    }

    int *dp1 = new int[n + 1];
    for (int i1 = 0; i1 < n + 1; ++i1) {
        dp1[i1] = 10000;
    }
    dp1[1] = 0;

    h = head[i];
    kol_ch = 0;
    while (h != -1) {
        int v = edg[h];

        for (int j = c; j >= 0; --j) {
            dp1[j] += 1;
            for (int k = min(j, kol[v]); k >= 1; --k) {
                if (dp1[j] > dp1[j - k] + dp[v][k]) {
                    dp1[j] = dp1[j - k] + dp[v][k];
                    for (int z = 0; z < kol_ch; ++z) {
                        ch[j][z] = ch[j - k][z];
                    }
                    ch[j][kol_ch] = k;
                }
                if (kol[i] == kol[v] + 1 && j == k + 1) {
                    break;
                }
            }
        }
        h = next[h];
        ++kol_ch;
    }

    h = head[i];

    kol_ch = 0;
    while (h != -1) {
        if (ch[c][kol_ch] == 0) {
            printf("%d ", num[h]);
        } else {
            out(edg[h], ch[c][kol_ch]);
        }
        h = next[h];
        ++kol_ch;
    }
}

int main() {
    //freopen("input.txt", "r", stdin);
    scanf("%d%d", &n, &m);

    for (int i = 0; i < n + 1; ++i) {
        for (int j = 0; j < n + 1; ++j) {
            dp[i][j] = 10000;
        }
        head[i] = -1;
        next[i] = -1;
        kol[i] = 1;
    }

    for (int i = 2; i < n + 1; ++i) {
        dp[i][0] = 1;
    }

    int a, b, p = -1;
    for (int i = 0; i < n - 1; ++i) {
        scanf("%d%d", &a, &b);
        ++p;
        next[p] = head[a];
        head[a] = p;
        edg[p] = b;
        num[p] = i + 1;

        ++p;
        next[p] = head[b];
        head[b] = p;
        edg[p] = a;
        num[p] = i + 1;
    }

    srand(8453);
    int root = max(1, rand() % n + 1);
    dfs(root, -1);

    int ans = dp[root][m];
    int v = root;
    for (int i = 1; i < n + 1; ++i) {
        if (dp[i][m] + 1 < ans) {
            ans = dp[i][m] + 1;
            v = i;
        }
    }

    printf("%d\n", ans);

    if (v != root) {
        p = parent[v];
        int h = head[p];
        while (h != -1) {
            if (edg[h] == v) {
                printf("%d ", num[h]);
                break;
            }
            h = next[h];
        }
    }
    out(v, m);
    return 0;
}
