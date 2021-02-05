kernel void add(global const int *a, global const int *b, global int *c) {
    *c = *a + *b;
}