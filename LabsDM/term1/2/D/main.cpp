#include <iostream>
#include <vector>
#include <algorithm>
#include <fstream>
#include <string>

using namespace std;

int main()
{
    freopen("lzw.in", "r", stdin);
    freopen("lzw.out", "w", stdout);
    string s, t="";
    cin >> s;

    int k = 26, last;
    vector<string> slov;

    for(int i = 0; i < 26; ++i){
        string tec;
        tec += ('a' + i);
        slov.push_back(tec);
       // cout << slov[i] << " ";
    }

    bool f;
    for(int i = 0; i < s.length(); ++i){
        t += s[i];
        f = false;
        for(int j = 0; j < k; ++j){
            if(slov[j] == t){
                f = true;
                last = j;
                break;
            }
        }

        if(f){
            continue;
        }
        cout << last << " ";
        ++k;
        slov.push_back(t);
        t = (s[i]);
        last = s[i]-'a';
    }

    cout << last;

    return 0;
}
