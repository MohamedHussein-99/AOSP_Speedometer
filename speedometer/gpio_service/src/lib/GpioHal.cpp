#include "GpioHal.h"

#include <fstream>

#include <string>

bool GpioHal::exportGpio(int pin) {
    std::ofstream exportFile("/sys/class/gpio/export");
    if (!exportFile) return false;
    exportFile << pin;
    return exportFile.good();
}

bool GpioHal::setGpioDirection(int pin, const std::string& direction) {

std::string directionPath = "/sys/class/gpio/gpio" + std::to_string(pin) + "/direction";
    std::ofstream directionFile(directionPath);
    if (!directionFile) return false;
    directionFile << direction;
    return directionFile.good();
}

bool GpioHal::setGpioValue(int pin, bool value) {
std::string valuePath = "/sys/class/gpio/gpio" + std::to_string(pin) + "/value";
    std::ofstream valueFile(valuePath);
    if (!valueFile) return false;
    valueFile << (value ? "1" : "0");
    return valueFile.good();
}

bool GpioHal::getGpioValue(int pin, bool &value) {
std::string valuePath = "/sys/class/gpio/gpio" + std::to_string(pin) + "/value";
    std::ifstream valueFile(valuePath);
    if (!valueFile) return false;
    int gpioValue;
    valueFile >> gpioValue;
    value = (gpioValue == 1);
    return valueFile.good();
}