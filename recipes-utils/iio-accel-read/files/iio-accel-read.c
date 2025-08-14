#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>
#include <ctype.h>
#include <unistd.h>

static int read_file(const char *path, char *buf, size_t sz) {
    FILE *f = fopen(path, "r");
    if (!f) return -1;
    size_t n = fread(buf, 1, sz-1, f);
    fclose(f);
    if (n == 0) return -1;
    buf[n] = 0;
    return 0;
}

static int is_accel_device(const char *devpath) {
    char namepath[256], name[256];
    snprintf(namepath, sizeof(namepath), "%s/name", devpath);
    if (read_file(namepath, name, sizeof(name))) return 0;
    for (char *p = name; *p; ++p) *p = (char)tolower(*p);
    return strstr(name, "accel") || strstr(name, "adxl") || strstr(name, "lis3") ||
           strstr(name, "mpu") || strstr(name, "mma");
}

int main(void) {
    const char *iiopath = "/sys/bus/iio/devices";
    DIR *d = opendir(iiopath);
    if (!d) { perror("open iio"); return 1; }

    char dev[256] = {0};
    struct dirent *e;
    while ((e = readdir(d))) {
        if (strncmp(e->d_name, "iio:device", 10) == 0) {
            char path[256];
            snprintf(path, sizeof(path), "%s/%s", iiopath, e->d_name);
            if (is_accel_device(path)) {
                snprintf(dev, sizeof(dev), "%s", path);
                break;
            }
        }
    }
    closedir(d);
    if (!dev[0]) { fprintf(stderr, "No IIO accelerometer found.\n"); return 2; }

    char p_scale[256], p_xr[256], p_yr[256], p_zr[256], buf[64];
    snprintf(p_scale, sizeof(p_scale), "%s/in_accel_scale", dev);
    snprintf(p_xr,    sizeof(p_xr),    "%s/in_accel_x_raw", dev);
    snprintf(p_yr,    sizeof(p_yr),    "%s/in_accel_y_raw", dev);
    snprintf(p_zr,    sizeof(p_zr),    "%s/in_accel_z_raw", dev);

    if (read_file(p_scale, buf, sizeof(buf))) { fprintf(stderr, "scale not found\n"); return 3; }
    const double scale = atof(buf);

    while (1) {
        if (read_file(p_xr, buf, sizeof(buf))) { perror("x_raw"); return 4; }
        const long xr = strtol(buf, NULL, 10);
        if (read_file(p_yr, buf, sizeof(buf))) { perror("y_raw"); return 4; }
        const long yr = strtol(buf, NULL, 10);
        if (read_file(p_zr, buf, sizeof(buf))) { perror("z_raw"); return 4; }
        const long zr = strtol(buf, NULL, 10);

        printf("x=%.3f  y=%.3f  z=%.3f (m/s^2)\n",
               xr * scale, yr * scale, zr * scale);
        fflush(stdout);
        usleep(200 * 1000);
    }
}
