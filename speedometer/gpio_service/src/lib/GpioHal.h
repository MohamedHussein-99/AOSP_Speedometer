#pragma once
#include <string>
class GpioHal {public:    bool exportGpio(int pin);
bool setGpioDirection(int pin, const std::string& direction);
bool setGpioValue(int pin, bool value);    
bool getGpioValue(int pin, bool &value);};