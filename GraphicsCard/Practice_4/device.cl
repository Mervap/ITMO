#define TILE_SIZE 32

kernel void add(global const float *a,
                global const float *b,
                global float *c,
                uint const M) {

    local float aLocalBlock[TILE_SIZE][TILE_SIZE];
    local float bLocalBlock[TILE_SIZE][TILE_SIZE];

    uint i = get_global_id(1);
    uint j = get_global_id(0);

    uint k = get_global_size(1);

    uint iLocal = get_local_id(1);
    uint jLocal = get_local_id(0);

    float sum = 0;
    for (uint tile_id = 0; tile_id < M / TILE_SIZE; ++tile_id) {

        uint tiled_col = tile_id * TILE_SIZE + jLocal;
        uint tiled_row = tile_id * TILE_SIZE + iLocal;

        aLocalBlock[iLocal][jLocal] = a[i * M + tiled_col];
        bLocalBlock[iLocal][jLocal] = b[tiled_row * k + j];

        barrier(CLK_LOCAL_MEM_FENCE);

        for (uint t = 0; t < TILE_SIZE; ++t) {
            sum += aLocalBlock[iLocal][t] * bLocalBlock[t][jLocal];
        }

        barrier(CLK_LOCAL_MEM_FENCE);
    }
    c[i * k + j] = sum;
}