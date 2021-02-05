kernel void add(global const float *a, global const float *b, global float *c, uint m) {
  uint i = get_global_id(0);
  uint j = get_global_id(1);
  uint k = get_global_size(1);
  float sum = 0;
  for (int l = 0; l < m; ++l) {
     sum += a[m * i + l] * b[k * l + j];
  }
  c[k * i + j] = sum;
}