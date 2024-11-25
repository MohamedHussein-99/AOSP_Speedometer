#include "include/UartHal.h"
#include <iostream>
#include <string>
#include <sstream>

int main() {
    UART_HAL uart("/dev/ttyS0");
    UART_Config config = {
        .baudRate = 9600,
        .dataBits = 8,
        .stopBits = 1,
        .parity = 'N'
    };

    if (!uart.init(config)) {
        std::cerr << "Failed to initialize UART" << std::endl;
        return 1;
    }

    const size_t bufferSize = 1024;
    uint8_t tempBuffer[bufferSize];
    std::string accumulatedData;
    std::string gprmcSentence;

    while (true) {
        size_t bytesRead = uart.read(tempBuffer, bufferSize);
        if (bytesRead > 0) {
            // Append new data to the accumulated buffer
            accumulatedData += std::string(reinterpret_cast<const char*>(tempBuffer), bytesRead);

            // Look for a complete line in the buffer
            std::istringstream stream(accumulatedData);
            std::string line;
            while (std::getline(stream, line)) {
                if (line.find("$GPRMC") == 0) {
                    gprmcSentence = line;
                    if (gprmcSentence.back() == '\n' || gprmcSentence.back() == '\r') {
                        break;
                    }
                }
            }

            // If we have the complete $GPRMC line, break
            if (!gprmcSentence.empty()) {
                break;
            }

            // Clear processed lines from the accumulated buffer
            accumulatedData = std::string(std::istreambuf_iterator<char>(stream), {});
        } else {
            std::cerr << "No data read from UART or an error occurred." << std::endl;
        }
    }

    if (!gprmcSentence.empty()) {
        std::cout << "Received GPRMC Sentence: " << gprmcSentence << std::endl;
    } else {
        std::cerr << "GPRMC sentence not found." << std::endl;
    }

    return 0;
}