#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>

using namespace std;

int main()
{

    //freopen("f.in", "r", stdin);
    //freopen("f.out", "w", stdout);

    string s, s1;
    cin >> s;
    cin >> s1;

    int l = (int) s.length();
    int l1 = (int) s1.length();

    vector<bool> was(l);
    was[0] = true;

    vector<bool> a(l + 1), b(l + 1);
    b[0] = true;

    int i = 0;
    while(i < l && s[i] == '*'){
        b[i + 1] = true;
        ++i;
    }

    for(int i = 1; i <= l1; ++i){
        a = b;
        b.assign(l + 1, false);
        for(int j = 1; j <= l; ++j){
            if(i == 1 && j == 1){
                a[j - 1] = true;
            }

            if(s[j - 1] == '?'){
                b[j] = a[j - 1];

            } else if (s[j - 1] == '*'){
                b[j] = was[j - 1];
            } else{
                b[j] = (s[j - 1] == s1[i - 1]) && a[j - 1];
            }

            was[j] = was[j] || b[j];
            //cout << j << " " << was[j] << "\n";
        }
    }

    /*if(l1 == 0){
        for(int i = 1; i <= l; ++i){
            b[i] = b[i-1] && (s[i - 1] == '*');
        }
    }*/
    b[l] ? cout << "YES" : cout << "NO";
    return 0;
}
