#include <stdio.h>
#include <CL/opencl.h>

const int STRING_SIZE = 128;
const int FILE_SIZE = 1024;

void check_error(void* val, const char *message) {
  if (val != NULL) {
    printf("%s: OK\n", message);
  }
  else {
    printf("%s: ERROR\n", message);
  }
}

int main() {
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
  FILE *file = fopen("../Practice_2/device.cl", "rb");
  size_t len = fread(filetext, sizeof(char), FILE_SIZE, file);
  fclose(file);

  cl_program program = clCreateProgramWithSource(context, 1, (const char **) &filetext_pointer, &len, NULL);
  check_error(program, "Program");

  cl_int code = clBuildProgram(program, 1, devices, "", NULL, NULL);
  if (code != CL_SUCCESS) {
    char buffer[2048];
    clGetProgramBuildInfo(program, devices[0], CL_PROGRAM_BUILD_LOG, sizeof(buffer), buffer, NULL);
    printf("Compilation FAILED: %s\n", buffer);
    exit(1);
  }
  else {
    printf("Compilation OK\n");
  }

  cl_kernel kernel = clCreateKernel(program, "add", NULL);
  check_error(kernel, "Kernel");

  cl_mem a = clCreateBuffer(context, CL_MEM_READ_ONLY, sizeof(cl_int), NULL, NULL);
  cl_mem b = clCreateBuffer(context, CL_MEM_READ_ONLY, sizeof(cl_int), NULL, NULL);
  cl_mem c = clCreateBuffer(context, CL_MEM_WRITE_ONLY, sizeof(cl_int), NULL, NULL);

  cl_int ha, hb;
  scanf("%d %d", &ha, &hb);
  clEnqueueWriteBuffer(queue, a, CL_NON_BLOCKING, 0, sizeof(cl_int), &ha, 0, NULL, NULL);
  clEnqueueWriteBuffer(queue, b, CL_NON_BLOCKING, 0, sizeof(cl_int), &hb, 0, NULL, NULL);

  clSetKernelArg(kernel, 0, sizeof(cl_mem), &a);
  clSetKernelArg(kernel, 1, sizeof(cl_mem), &b);
  clSetKernelArg(kernel, 2, sizeof(cl_mem), &c);

  size_t global_work_size = 1;
  cl_event event;
  clEnqueueNDRangeKernel(queue, kernel, 1, NULL, &global_work_size, NULL, 0, NULL, &event);
  check_error(event, "Event");

  cl_int res = 0;
  clEnqueueReadBuffer(queue, c, CL_BLOCKING, 0, sizeof(cl_int), &res, 0, NULL, NULL);
  printf("Res: %d\n", res);

  cl_ulong st, fn;
  clGetEventProfilingInfo(event, CL_PROFILING_COMMAND_START, sizeof(cl_ulong), &st, NULL);
  clGetEventProfilingInfo(event, CL_PROFILING_COMMAND_END, sizeof(cl_ulong), &fn, NULL);
  printf("Work time: %ldns", fn - st);

  free(platforms);
  free(devices);
}