#pragma once
#include <cstdint>
#include "bytecodes.hpp"
#include <Windows.h>
#include <cstring>
#include <cstdio>
#include <iostream>

typedef uint8_t UI8;
typedef uint16_t UI16;
typedef uint32_t UI32;
typedef int32_t I32;

enum {
	STATUS_OK = 0,
	STATUS_ERROR,
	STATUS_NEED_MORE,
	STATUS_INVALID
};

enum {
	REPLY_NEED = 0,
	REPLY_NO_NEED
};

enum {
	FORMAT_8I = 0,
	FORMAT_16I,
	FORMAT_32I,
	FORMAT_FLOAT
};

typedef struct {
	UI8 type, mode;
} deviceTypemode_t;

class EV3 {
	//int(*_send)(void*, int);
	//int(*_recv)(void*, int);
	bool connected;
	UI16 mnum;
	int lMotor, rMotor;
	HANDLE bluetoothHandle;
	int _send(void* data, int packSize);
	int _recv(void* data, int maxSize);


	void setSend(int(*_send)(void* data, int size));
	void setRecv(int(*_recv)(void* data, int max));

public:
	enum MOTORS{
		MOTOR_A = 0x01,
		MOTOR_B = 0x02,
		MOTOR_C = 0x04,
		MOTOR_D = 0x08
	};
	enum PORTS{
		PORT_1 = 0x00,
		PORT_2,
		PORT_3,
		PORT_4,
		PORT_A = 0x10,
		PORT_B,
		PORT_C,
		PORT_D
	};

	enum {
		STOP_FLOAT = 0,
		STOP_BRAKE
	};

	void outputSpeed(UI8 motors, int val);
	void outputPower(UI8 motors, int val);
	void outputStart(UI8 motors);
	void outputStop(UI8 motors, int type);
	deviceTypemode_t inputDeviceTypemode(UI8 port);
	UI8 inputFormat(UI8 port);
	I32 getRaw(UI8 port);

	//EV3 device;
	EV3(int leftMotor, int rightMotor);
	void connect(unsigned int comPort);
	void motors(int leftSpeed, int rightSpeed);
	~EV3();
};