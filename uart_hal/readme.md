# UART HAL Module

It is a module to support communication with a GPS device. The module is designed to configure, read, and write data through a UART interface on Linux based systems like AOSP with test app to serve the purpose which it it written for.

## /boot/cmdline.txt

```txt
no_console_suspend root=/dev/ram0 rootwait androidboot.hardware=rpi4 androidboot.selinux=permissive console=tty1
```

The `console=tty1` parameter directs kernel messages and the Linux console to tty1, leaving tty0 free for other purposes—such as uart communication avoiding conflict.

## /boot/config.txt

```txt
# Serial console
enable_uart=1
```
