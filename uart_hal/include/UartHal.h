#ifndef UART_HAL_H
#define UART_HAL_H

#include <stdint.h>
#include <stddef.h>  

typedef struct {
    uint32_t baudRate;    // Baud rate (9600, 115200)
    uint8_t dataBits;     // Number of data bits (7, 8)
    uint8_t stopBits;     // Number of stop bits (1, 2)
    char parity;          // Parity ('N', 'O', 'E')
} UART_Config;

class UART_HAL {
public:
    UART_HAL(const char* device);
    ~UART_HAL();

    bool init(const UART_Config& config);  
    size_t write(const uint8_t* data, size_t length);  
    size_t read(uint8_t* buffer, size_t length);     

private:
    int uartFd;  
    bool configure(const UART_Config& config);  
};

#endif
