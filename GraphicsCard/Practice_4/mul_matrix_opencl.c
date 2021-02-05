#include <stdio.h>
#include <CL/opencl.h>
#include <math.h>
#include <memory.h>
#include <assert.h>

const int STRING_SIZE = 128;
const int FILE_SIZE = 1024;

void check_error(void *val, const char *message) {
  if (val != NULL) {
    printf("%s: OK\n", message);
  } else {
    printf("%s: ERROR\n", message);
  }
}

void printMatrix(FILE *f, cl_float *a, size_t size_y, size_t size_x, const char *name) {
//  fprintf(f, "%s:\n", name);
  for (int i = 0; i < size_y; ++i) {
    for (int j = 0; j < size_x; ++j) {
      fprintf(f, " %.0f", a[i * size_x + j]);
    }
    fprintf(f, "\n");
  }
  fprintf(f, "\n");
}

void fillRandom(cl_float *a, size_t n, size_t m) {
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < m; ++j) {
      a[i * m + j] = rand() % 10 /*/ (cl_float) rand()*/;
    }
  }
}


int check(const cl_float *a, const cl_float *b, const cl_float *res, int n, int m, int k) {
  cl_float *c = (cl_float *) malloc(n * k * sizeof(cl_float));
  memset(c, 0, n * k);
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < k; ++j) {
      for (int l = 0; l < m; ++l) {
        c[k * i + j] += a[m * i + l] * b[k * l + j];
      }
    }
  }

  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < k; ++j) {
      if (fabsf(c[i * k + j] - res[i * k + j]) > 10e-6) {
        return 0;
      }
    }
  }

  return 1;
}

//int main() {
int main(int argc, char *argv[]) {
  unsigned int cnt = 0;
  clGetPlatformIDs(0, 0, &cnt);
  cl_platform_id *platforms = (cl_platform_id *) malloc(sizeof(cl_platform_id) * cnt);
  clGetPlatformIDs(cnt, platforms, 0);

  char name[STRING_SIZE];
  char vendor[STRING_SIZE];
  char version[STRING_SIZE];
  for (int i = 0; i < cnt; i++) {
    clGetPlatformInfo(platforms[i], CL_PLATFORM_VENDOR, STRING_SIZE, vendor, NULL);
    clGetPlatformInfo(platforms[i], CL_PLATFORM_NAME, STRING_SIZE, name, NULL);
    clGetPlatformInfo(platforms[i], CL_PLATFORM_VERSION, STRING_SIZE, version, NULL);
    printf("Platform %d: %s %s %s\n", i, vendor, name, version);
  }

  cl_device_id *devices = (cl_device_id *) malloc(sizeof(cl_device_id) * cnt);
  clGetDeviceIDs(platforms[2], CL_DEVICE_TYPE_GPU, 1, devices, NULL);

  cl_context context = clCreateContext(NULL, 1, devices, NULL, NULL, NULL);
  check_error(context, "Context");

  cl_command_queue queue = clCreateCommandQueue(context, devices[0], CL_QUEUE_PROFILING_ENABLE, NULL);
  check_error(queue, "Queue");

  char filetext[FILE_SIZE], *filetext_pointer = filetext;
  FILE *file = fopen("../Practice_4/device.cl", "rb");
  size_t len = fread(filetext, sizeof(char), FILE_SIZE, file);
  fclose(file);

  cl_program program = clCreateProgramWithSource(context, 1, (const char **) &filetext_pointer, &len, NULL);
  check_error(program, "Program");

  cl_int code = clBuildProgram(program, 1, devices, "", NULL, NULL);
  if (code != CL_SUCCESS) {
    char buffer[40960];
    clGetProgramBuildInfo(program, devices[0], CL_PROGRAM_BUILD_LOG, sizeof(buffer), buffer, NULL);
    printf("Compilation FAILED: %s\n", buffer);
    exit(1);
  } else {
    printf("Compilation OK\n");
  }

  cl_kernel kernel = clCreateKernel(program, "add", NULL);
  check_error(kernel, "Kernel");

  size_t n, m, k;
  n = atoi(argv[1]);
  m = atoi(argv[2]);
  k = atoi(argv[3]);
//  scanf("%zd %zd %zd", &n, &m, &k);
  size_t fstMatSize = n * m * sizeof(cl_float);
  size_t sndMatSize = m * k * sizeof(cl_float);
  size_t resMatSize = n * k * sizeof(cl_float);
  cl_mem a = clCreateBuffer(context, CL_MEM_READ_ONLY, fstMatSize, NULL, NULL);
  cl_mem b = clCreateBuffer(context, CL_MEM_READ_ONLY, sndMatSize, NULL, NULL);
  cl_mem c = clCreateBuffer(context, CL_MEM_WRITE_ONLY, resMatSize, NULL, NULL);

  cl_float *ha = malloc(fstMatSize);
  cl_float *hb = malloc(sndMatSize);
  cl_float *result = malloc(resMatSize);

  fillRandom(ha, n, m);
  fillRandom(hb, m, k);

  code = clEnqueueWriteBuffer(queue, a, CL_NON_BLOCKING, 0, fstMatSize, ha, 0, NULL, NULL);
  if (code != CL_SUCCESS) {
    exit(1);
  } else {
    printf("Run OK\n");
  }
  code = clEnqueueWriteBuffer(queue, b, CL_NON_BLOCKING, 0, sndMatSize, hb, 0, NULL, NULL);
  if (code != CL_SUCCESS) {
    exit(1);
  } else {
    printf("Run OK\n");
  }

  clSetKernelArg(kernel, 0, sizeof(cl_mem), &a);
  clSetKernelArg(kernel, 1, sizeof(cl_mem), &b);
  clSetKernelArg(kernel, 2, sizeof(cl_mem), &c);
  clSetKernelArg(kernel, 3, sizeof(cl_int), &m);

  cl_event event;
  size_t work_size[] = {n, k};
  const size_t tile_size = 32;
  size_t local_size[] = {tile_size, tile_size};
  assert(work_size[0] % local_size[0] == 0 && work_size[1] % local_size[1] == 0);

  code = clEnqueueNDRangeKernel(queue, kernel, 2, NULL, work_size, local_size, 0, NULL, &event);
  if (code != CL_SUCCESS) {
    exit(1);
  } else {
    printf("Run OK\n");
  }
  check_error(event, "Event");

  code = clEnqueueReadBuffer(queue, c, CL_BLOCKING, 0, resMatSize, result, 0, NULL, NULL);
  if (code != CL_SUCCESS) {
    exit(1);
  } else {
    printf("Run OK\n");
  }

//  FILE *fp = fopen("../Practice_4/out", "w");
//  printMatrix(fp, ha, n, m, "A");
//  printMatrix(fp, hb, m, k, "B");
//  printMatrix(fp, result, n, k, "Res");
/*  if (check(ha, hb, res, n, m, k) == 0) {
    printf("Fail computation\n");
  }*/

  cl_ulong st, fn;
  clGetEventProfilingInfo(event, CL_PROFILING_COMMAND_START, sizeof(cl_ulong), &st, NULL);
  clGetEventProfilingInfo(event, CL_PROFILING_COMMAND_END, sizeof(cl_ulong), &fn, NULL);
  printf("Work time: %ldms", (fn - st) / 1000000);

  free(platforms);
  free(devices);
}

/**
* My
* Description
* @code
* @endcode
* the
* end
*/
void foo(int a, int b, int c);
