

////////////////////////////////////////
//      Work but not use uart
////////////////////////////////////////
// #include "GPS.h"
// #include <iostream>
// #include <fcntl.h>
// #include <unistd.h>
// #include <cstring>
// #include <sstream>
// #include <cstdlib>
// #include <vector>
// #include <chrono>
// #include <cmath> // For std::round

// GPS::GPS(const std::string& uartDevice)
//     : latitude(0.0), longitude(0.0), speed(0.0), dataValid(false) {
//     uart_fd = open(uartDevice.c_str(), O_RDWR | O_NOCTTY | O_NDELAY);
//     if (uart_fd == -1) {
//         std::cerr << "Failed to open UART device: " << uartDevice << std::endl;
//         exit(EXIT_FAILURE);
//     }
//     fcntl(uart_fd, F_SETFL, 0); // Set file access mode
// }

// GPS::~GPS() {
//     if (uart_fd != -1) {
//         close(uart_fd);
//     }
// }

// void GPS::startReading() {
//     std::string gprmcSentence = getGPRMCSentence();
//     parseGPRMC(gprmcSentence);
// }

// std::string GPS::getGPRMCSentence() {
//     const size_t bufferSize = 1024;
//     char tempBuffer[bufferSize];
//     memset(tempBuffer, 0, bufferSize);

//     while (true) {
//         ssize_t bytesRead = read(uart_fd, tempBuffer, bufferSize - 1);
//         if (bytesRead > 0) {
//             rawBuffer.append(tempBuffer, bytesRead);

//             // Look for complete lines in the buffer
//             size_t newlinePos;
//             while ((newlinePos = rawBuffer.find('\n')) != std::string::npos) {
//                 std::string sentence = rawBuffer.substr(0, newlinePos + 1);
//                 rawBuffer.erase(0, newlinePos + 1);

//                 // Return the first $GPRMC sentence
//                 if (sentence.find("$GPRMC") == 0) {
//                     return sentence;
//                 }
//             }
//         } else {
//             std::cout << "UART ERROR" << std::endl;
//         }
//     }
// }

// void GPS::parseGPRMC(const std::string& sentence) {
//     std::istringstream stream(sentence);
//     std::string token;
//     std::vector<std::string> fields;

//     while (std::getline(stream, token, ',')) {
//         fields.push_back(token);
//     }

//     if (fields.size() >= 10 && fields[2] == "A") { // Check for valid data
//         latitude = convertToDecimalDegrees(fields[3], fields[4]);
//         longitude = convertToDecimalDegrees(fields[5], fields[6]);

//         // Convert speed from knots to km/h
//         speed = std::stod(fields[7]) * 1.852; 

//         dataValid = true;
//     } else {
//         dataValid = false;
//     }
// }

// double GPS::convertToDecimalDegrees(const std::string& nmeaCoord, const std::string& direction) {
//     // Degrees is the integer part, minutes is the fractional part
//     double degrees = std::stod(nmeaCoord.substr(0, nmeaCoord.find('.') - 2));
//     double minutes = std::stod(nmeaCoord.substr(nmeaCoord.find('.') - 2));
    
//     // Convert to decimal degrees
//     double decimal = degrees + (minutes / 60.0);

//     if (direction == "S" || direction == "W") {
//         decimal = -decimal;
//     }

//     return decimal;
// }

// // Return scaled integer latitude (multiplied by 1000000)
// float GPS::getLat() {
//     std::lock_guard<std::mutex> lock(dataMutex);
//    // return static_cast<int>(std::round(latitude * 10000)); // Return scaled value
//     return static_cast<float>(latitude); // Return as float
// }

// // Return scaled integer longitude (multiplied by 1000000)
// float GPS::getLong() {
//     std::lock_guard<std::mutex> lock(dataMutex);
//     //return static_cast<int>(std::round(longitude * 10000)); // Return scaled value
//     return static_cast<float>(longitude); // Return as float
// }

// // Return speed, rounded to nearest integer
// float GPS::getSpeed() {
//     std::lock_guard<std::mutex> lock(dataMutex);
//     //return static_cast<int>(std::round(speed)); // Round speed as well
//     return static_cast<float>(speed); // Return as float
// }

// bool GPS::isDataValid() {
//     std::lock_guard<std::mutex> lock(dataMutex);
//     return dataValid;
// }


#include "GPS.h"
#include <iostream>
#include <cstring>
#include <sstream>
#include <vector>
#include <cmath> // For std::round

GPS::GPS(const std::string& uartDevice)
    : latitude(0.0), longitude(0.0), speed(0.0), dataValid(false){
    uart = std::make_unique<UART_HAL>(uartDevice.c_str());

    if (!uart->init({9600, 8, 1, 'N'})) {
        std::cerr << "Failed to initialize UART with given configuration" << std::endl;
        exit(EXIT_FAILURE);
    }
}

GPS::~GPS() {
    // UART_HAL destructor will automatically close the UART connection
}

void GPS::startReading() {
    std::string gprmcSentence = getGPRMCSentence();
    parseGPRMC(gprmcSentence);
}

std::string GPS::getGPRMCSentence() {
    const size_t bufferSize = 1024;
    uint8_t tempBuffer[bufferSize];
    memset(tempBuffer, 0, bufferSize);

    while (true) {
        size_t bytesRead = uart->read(tempBuffer, bufferSize);
        if (bytesRead > 0) {
            rawBuffer.append(reinterpret_cast<char*>(tempBuffer), bytesRead);

            // Look for complete lines in the buffer
            size_t newlinePos;
            while ((newlinePos = rawBuffer.find('\n')) != std::string::npos) {
                std::string sentence = rawBuffer.substr(0, newlinePos + 1);
                rawBuffer.erase(0, newlinePos + 1);

                // Return the first $GPRMC sentence
                if (sentence.find("$GPRMC") == 0) {
                    return sentence;
                }
            }
        } else {
            std::cerr << "UART read error" << std::endl;
        }
    }
}

void GPS::parseGPRMC(const std::string& sentence) {
    std::istringstream stream(sentence);
    std::string token;
    std::vector<std::string> fields;

    while (std::getline(stream, token, ',')) {
        fields.push_back(token);
    }

    if (fields.size() >= 10 && fields[2] == "A") { // Check for valid data
        latitude = convertToDecimalDegrees(fields[3], fields[4]);
        longitude = convertToDecimalDegrees(fields[5], fields[6]);

        // Convert speed from knots to km/h
        speed = std::stod(fields[7]) * 1.852; 

        dataValid = true;
    } else {
        dataValid = false;
    }
}

double GPS::convertToDecimalDegrees(const std::string& nmeaCoord, const std::string& direction) {
    // Degrees is the integer part, minutes is the fractional part
    double degrees = std::stod(nmeaCoord.substr(0, nmeaCoord.find('.') - 2));
    double minutes = std::stod(nmeaCoord.substr(nmeaCoord.find('.') - 2));
    
    // Convert to decimal degrees
    double decimal = degrees + (minutes / 60.0);

    if (direction == "S" || direction == "W") {
        decimal = -decimal;
    }

    return decimal;
}

float GPS::getLat() {
    std::lock_guard<std::mutex> lock(dataMutex);
    return static_cast<float>(latitude); // Return as float
}

float GPS::getLong() {
    std::lock_guard<std::mutex> lock(dataMutex);
    return static_cast<float>(longitude); // Return as float
}

float GPS::getSpeed() {
    std::lock_guard<std::mutex> lock(dataMutex);
    return static_cast<float>(speed); // Return as float
}

bool GPS::isDataValid() {
    std::lock_guard<std::mutex> lock(dataMutex);
    return dataValid;
}