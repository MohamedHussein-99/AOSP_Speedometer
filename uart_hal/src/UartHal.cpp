#include "include/UartHal.h"
#include <iostream>
#include <fcntl.h>   
#include <unistd.h>   
#include <termios.h>  

UART_HAL::UART_HAL(const char* device) {
    uartFd = open(device, O_RDWR | O_NOCTTY);
    if (uartFd < 0) {
        std::cerr << "Failed to open UART device: " << device << std::endl;
    }
}

UART_HAL::~UART_HAL() {
    if (uartFd >= 0) {
        close(uartFd);
    }
}

bool UART_HAL::init(const UART_Config& config) {
    return configure(config);
}

bool UART_HAL::configure(const UART_Config& config) {
    struct termios uartSettings;
    if (tcgetattr(uartFd, &uartSettings) != 0) {
        std::cerr << "Failed to get UART attributes" << std::endl;
        return false;
    }

    speed_t speed;
    switch (config.baudRate) {
        case 9600: speed = B9600; break;
        case 115200: speed = B115200; break;
        default:
            std::cerr << "Unsupported baud rate: " << config.baudRate << std::endl;
            return false;
    }
    cfsetospeed(&uartSettings, speed);
    cfsetispeed(&uartSettings, speed);

    uartSettings.c_cflag &= ~CSIZE;
    if (config.dataBits == 7) {
        uartSettings.c_cflag |= CS7;
    } else if (config.dataBits == 8) {
        uartSettings.c_cflag |= CS8;
    } else {
        std::cerr << "Unsupported data bits: " << config.dataBits << std::endl;
        return false;
    }

    if (config.stopBits == 2) {
        uartSettings.c_cflag |= CSTOPB;
    } else {
        uartSettings.c_cflag &= ~CSTOPB;
    }

    if (config.parity == 'N') {
        uartSettings.c_cflag &= ~PARENB;
    } else if (config.parity == 'O') {
        uartSettings.c_cflag |= PARENB | PARODD;
    } else if (config.parity == 'E') {
        uartSettings.c_cflag |= PARENB;
        uartSettings.c_cflag &= ~PARODD;
    } else {
        std::cerr << "Unsupported parity: " << config.parity << std::endl;
        return false;
    }

    if (tcsetattr(uartFd, TCSANOW, &uartSettings) != 0) {
        std::cerr << "Failed to set UART attributes" << std::endl;
        return false;
    }

    return true;
}

size_t UART_HAL::write(const uint8_t* data, size_t length) {
    if (uartFd < 0) {
        return 0;
    }
    return ::write(uartFd, data, length);
}

size_t UART_HAL::read(uint8_t* buffer, size_t length) {
    if (uartFd < 0) {
        return 0;
    }
    return ::read(uartFd, buffer, length);
}