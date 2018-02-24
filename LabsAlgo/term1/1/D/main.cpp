#include <iostream>
#include <fstream>
#include <cstdlib>
#ifdef _DEBUG
#include <crtdbg.h>
#define _CRTDBG_MAP_ALLOC
#endif

using namespace std;

int len=0;
void add(int k);
int pop();
int *a, *nextt, *last, *tmp, *tmp1, *tmp2;
int h,p=0,s=10;

typedef struct _CrtMemBlockHeader


int main()
{
    _CrtMemState _ms;
    _CrtMemCheckpoint(&_ms);
    freopen("queue2.in", "r", stdin);
    freopen("queue2.out", "w", stdout);

    a = (int* ) malloc (s*sizeof(int));
    nextt= (int* ) malloc (s*sizeof(int));
    last= (int* ) malloc (s*sizeof(int));


    h=-1;
    int n,k;
    char c;


    ios_base::sync_with_stdio(false);
    cout.tie(0);
    cin.tie(0);

    cin >> n;
    for(int i=0; i<n; i++){
        cin >> c;
        if(c=='+'){
            cin >> k;
            add(k);
        }
        else{
            cout << pop() << "\n";
        }
    }

     _CrtMemDumpAllObjectsSince(&_ms);
     _CrtSetReportMode( _CRT_WARN, _CRTDBG_MODE_FILE );
    _CrtSetReportFile( _CRT_WARN, _CRTDBG_FILE_STDOUT );
    return 0;
}

void add(int k){

    if(h==-1){
        h=p;
        nextt[p]=-1;
        last[p]=p;
    }
    else{
        nextt[p]=-1;
        nextt[last[h]]=p;
        last[p]=last[h];
        last[h]=p;
    }
    a[p]=k;
    p++;
    len++;

    if(p>=s && len!=0){
        s=s*2;
        tmp=a;
        tmp1=nextt;
        tmp2=last;

        a = (int* ) malloc (s*sizeof(int));
        nextt= (int* ) malloc (s*sizeof(int));
        last= (int* ) malloc (s*sizeof(int));

        int z=h;
        while (z!=-1){
            a[z-h]=tmp[z];
            nextt[z-h]=tmp1[z]-h;
            last[z-h]=tmp2[z]-h;
            z=tmp1[z];
        }

        free(tmp);
        free(tmp1);
        free(tmp2);
        h=0;
        p=len;
        nextt[p-1]=-1;

    }

}

int pop(){
    int z1 = a[h];
    if(nextt[h]!=-1){
        last[nextt[h]]=last[h];
    }
    h=nextt[h];
    len--;

    if(len!=0 && len<=s/4){
        s=s/2;
        tmp=a;
        tmp1=nextt;
        tmp2=last;

        a = (int* ) malloc (s*sizeof(int));
        nextt= (int* ) malloc (s*sizeof(int));
        last= (int* ) malloc (s*sizeof(int));

        int z=h;
        while (z!=-1){
            a[z-h]=tmp[z];
            nextt[z-h]=tmp1[z]-h;
            last[z-h]=tmp2[z]-h;
            z=tmp1[z];
        }

        free(tmp);
        free(tmp1);
        free(tmp2);
        h=0;
        p=len;

        nextt[p-1]=-1;
    }
    return z1;
}
