#include <iostream>

using namespace std;

int main()
{
    int n;
    cin >> n;

    int k = 2;
    while (k < n) {
        k *= 2;
    }

    string a[] = {"Aa", "BB"};
    for (int i = 0; i < n; ++i) {
        int t = k / 2;
        while (t != 0) {
            cout << a[(i & t) != 0];
            t /= 2;
        }
        cout << "\n";
    }

    return 0;
}
