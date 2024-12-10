
// #ifndef GPS_H
// #define GPS_H

// #include <string>
// #include <mutex>

// class GPS {
// public:
//     // Constructor: takes the UART device path
//     GPS(const std::string& uartDevice);

//     // Destructor: closes the UART device
//     ~GPS();

//     // Starts reading data from the GPS device
//     void startReading();

//     // Returns the current latitude as a float
//     float getLat();

//     // Returns the current longitude as a float
//     float getLong();

//     // Returns the current speed in km/h as a float
//     float getSpeed();

//     // Checks if the data from GPS is valid
//     bool isDataValid();

// private:
//     // Parses a GPRMC sentence and extracts relevant data
//     void parseGPRMC(const std::string& sentence);

//     // Reads a GPRMC sentence from the UART device
//     std::string getGPRMCSentence();

//     // Converts NMEA latitude/longitude to decimal degrees
//     double convertToDecimalDegrees(const std::string& nmeaCoord, const std::string& direction);

//     // UART file descriptor
//     int uart_fd;

//     // Raw buffer for reading from the UART
//     std::string rawBuffer;

//     // Data fields
//     double latitude;   // Latitude in decimal degrees
//     double longitude;  // Longitude in decimal degrees
//     double speed;      // Speed in km/h
//     bool dataValid;    // Data validity flag

//     // Mutex for thread-safe access to data fields
//     std::mutex dataMutex;
// };

// #endif // GPS_H

#ifndef GPS_H
#define GPS_H

#include <string>
#include <mutex>
#include "UartHal.h"


class GPS {
public:
    GPS(const std::string& uartDevice); 
    ~GPS();

    // Starts reading data from the GPS device
    void startReading();

    // Returns the current latitude as a float
    float getLat();

    // Returns the current longitude as a float
    float getLong();

    // Returns the current speed in km/h as a float
    float getSpeed();

    // Checks if the data from GPS is valid
    bool isDataValid();

private:

    std::unique_ptr<UART_HAL> uart; // Pointer to UART_HAL for UART operations

    // Parses a GPRMC sentence and extracts relevant data
    void parseGPRMC(const std::string& sentence);

    // Reads a GPRMC sentence from the UART device
    std::string getGPRMCSentence();

    // Converts NMEA latitude/longitude to decimal degrees
    double convertToDecimalDegrees(const std::string& nmeaCoord, const std::string& direction);

    // UART file descriptor
    // int uart_fd;

    // Raw buffer for reading from the UART
    std::string rawBuffer;

    // Data fields
    double latitude;   // Latitude in decimal degrees
    double longitude;  // Longitude in decimal degrees
    double speed;      // Speed in km/h
    bool dataValid;    // Data validity flag

    // Mutex for thread-safe access to data fields
    std::mutex dataMutex;
};

#endif // GPS_H