#include <fcntl.h>
#include <stdio.h>
#include <sys/ioctl.h>
#include <unistd.h>

int main(void)
{
    int fd = open("mydevice", O_RDWR);
    if (fd < 0) {
        perror("open");
        return 1;
    }

    int rc = ioctl(fd, 1, 2);
    if (rc < 0) {
        perror("ioctl");
    }

    (void) close(fd);

    return 0;
}
