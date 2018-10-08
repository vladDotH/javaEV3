#include "EV3.hpp"



void printError(const char *str) {
	fprintf(stderr, "%s\n", str);
}

void freeMessage(UI8 *ptr) {
	if (ptr)
		free(ptr);
}

UI8* extractMessage(UI8 *in, UI16 size, UI16 *msize, UI16 *mnum, UI8 *status, UI16 *spos) {
	UI16 m;
	UI8 *r;
	*spos = 0;

	if (((UI16*)in)[0] > size && size < 2) {
		*status = STATUS_NEED_MORE;
		return NULL;

	}
	if (((UI16*)in)[0] < 3) {
		printError("Message size is invalid. Skipping message...");
		*status = STATUS_INVALID;
		*spos = 2;
		return NULL;
	}

	*mnum = ((UI16*)in)[1];

	if (in[4] == 0x04)
		*status = STATUS_ERROR;
	else if (in[4] == 0x02)
		*status = STATUS_OK;
	else {
		printError("Message status is invalid. Skipping message...");
		*status = STATUS_INVALID;
		*spos = 5;
		return NULL;
	}

	r = (UI8*)malloc(((UI16*)in)[0] - 3);
	if (!r) {
		printError("Message allocation error. Can't extract message");
		return NULL;
	}
	*msize = ((UI16*)in)[0] - 3;
	memcpy(r, &in[5], *msize);
	return r;
}


UI8* packMessage(UI8 *body, UI16 bodySize, UI16 *msize, UI16 mnum, UI8 rep, UI16 global, UI8 local) {
	UI8* r;
	UI16 t;
	*msize = bodySize + 7;
	r = (UI8*)malloc(*msize);
	if (!r) {
		printError("Message allocation error. Can't pack message");
		return NULL;
	}

	memcpy(&r[7], body, bodySize);
	((UI16*)r)[0] = *msize - 2;
	((UI16*)r)[1] = mnum;

	if (rep == REPLY_NEED)
		r[4] = 0x00;
	else
		r[4] = 0x80;

	t = (global & 0xFF) | ((global & 0x03) | (local << 2));
	r[5] = t & 0xFF;
	r[6] = (t & 0xFF00) >> 8;
	return r;
}



void EV3::outputSpeed(UI8 motors, int val) {
	if (val > 100) val = 100;
	if (val < -100) val = -100;
	UI8 cmd[] = { opOUTPUT_SPEED, LC0(0), LC0(motors), LC1(val) };

	UI16 msize;
	UI8 *r = packMessage(cmd, sizeof(cmd), &msize, mnum, REPLY_NO_NEED, 0, 0);
	_send(r, msize);
}

void EV3::outputPower(UI8 motors, int val) {
	if (val > 100) val = 100;
	if (val < -100) val = -100;
	UI8 cmd[] = { opOUTPUT_POWER, LC0(0), LC0(motors), LC1(val) };

	UI16 msize;
	UI8 *r = packMessage(cmd, sizeof(cmd), &msize, mnum, REPLY_NO_NEED, 0, 0);
	_send(r, msize);
}

void EV3::outputStart(UI8 motors) {
	UI8 cmd[] = { opOUTPUT_START, LC0(0), LC0(motors) };

	UI16 msize;
	UI8 *r = packMessage(cmd, sizeof(cmd), &msize, mnum, REPLY_NO_NEED, 0, 0);
	_send(r, msize);
}

void EV3::outputStop(UI8 motors, int type) {
	UI8 cmd[] = { opOUTPUT_STOP, LC0(0), LC0(motors), LC0(type) };

	UI16 msize;
	UI8 *r = packMessage(cmd, sizeof(cmd), &msize, mnum, REPLY_NO_NEED, 0, 0);
	_send(r, msize);
}

deviceTypemode_t EV3::inputDeviceTypemode(UI8 port) {
	UI8 cmd[] = { opINPUT_DEVICE, GET_TYPEMODE, LC0(0), LC0(port), GV0(0), GV0(1) };
	deviceTypemode_t rr;

	UI16 msize, read, tmnum, off = 0;
	UI8 flag = STATUS_NEED_MORE, buf[256];
	UI8 *r = packMessage(cmd, sizeof(cmd), &msize, mnum, REPLY_NEED, 2, 0);

	_send(r, msize);
	freeMessage(r);
	r = NULL;
	Sleep(1);
	do {
		if (r)
			freeMessage(r);

		read = _recv(buf, 256);
		r = extractMessage(buf + off, read, &msize, &tmnum, &flag, &off);
	} while (flag != STATUS_OK && flag != STATUS_ERROR);

	//std::cout << ((flag == STATUS_OK) ? ("(OK)\n") : ("(ERROR)")) << endl;
	rr.mode = r[0];
	rr.type = r[1];
	freeMessage(r);
	return rr;
}


UI8 EV3::inputFormat(UI8 port) {
	UI8 cmd[] = { opINPUT_DEVICE, GET_FORMAT, LC0(0), LC0(port), GV0(0), GV0(1), GV0(2), GV0(3) };
	UI8 rr;

	UI16 msize, read, tmnum, off = 0;
	UI8 flag = STATUS_NEED_MORE, buf[256];
	UI8 *r = packMessage(cmd, sizeof(cmd), &msize, mnum, REPLY_NEED, 4, 0);

	_send(r, msize);
	freeMessage(r);
	r = NULL;
	Sleep(1);
	do {
		if (r)
			freeMessage(r);

		read = _recv(buf, 256);
		r = extractMessage(buf + off, read, &msize, &tmnum, &flag, &off);
	} while (flag != STATUS_OK && flag != STATUS_ERROR);

	//std::cout << ((flag == STATUS_OK) ? ("(OK)\n") : ("(ERROR)")) << endl;
	rr = r[2];
	freeMessage(r);
	return rr;
}

I32 EV3::getRaw(UI8 port) {
	uint8_t cmd[] = { opINPUT_DEVICE, GET_RAW, LC0(0), LC0(port), GV4(0) };
	UI32 rr;

	UI16 msize, read, tmnum, off = 0;
	UI8 flag = STATUS_NEED_MORE, buf[256];
	UI8 *r = packMessage(cmd, sizeof(cmd), &msize, mnum, REPLY_NEED, 4, 0);

	_send(r, msize);
	freeMessage(r);
	r = NULL;
	Sleep(1);
	do {
		if (r)
			freeMessage(r);

		read = _recv(buf, 256);
		r = extractMessage(buf + off, read, &msize, &tmnum, &flag, &off);
	} while (flag != STATUS_OK && flag != STATUS_ERROR);

	//std::cout << ((flag == STATUS_OK) ? ("(OK)\n") : ("(ERROR)")) << endl;
	rr = ((UI32 *)r)[0];
	freeMessage(r);
	return rr;
}


void EV3::setSend(int(*_send)(void* data, int size)) {
	//this->_send = _send;
}
void EV3::setRecv(int(*_recv)(void* data, int max)) {
	//this->_recv = _recv;
}




EV3::EV3(int leftMotor, int rightMotor) {
	lMotor = leftMotor;
	rMotor = rightMotor;
	mnum = 0;
}

void EV3::connect(unsigned int comPort) {
	bool connected_ = 1;
	connected = 0;

	char comPortString[10];
	sprintf_s(comPortString, "%d", comPort);
	std::string fileName = "\\\\.\\COM" + std::string(comPortString);

	bluetoothHandle = CreateFile( (const wchar_t*) fileName.c_str(),
		GENERIC_READ | GENERIC_WRITE,
		0,    // comm devices must be opened w/exclusive-access
		NULL, // no security attributes
		OPEN_EXISTING, // comm devices must use OPEN_EXISTING
		0,    // overlapped I/O
		NULL  // hTemplate must be NULL for comm devices
		);

	if (bluetoothHandle == INVALID_HANDLE_VALUE)
		connected_ = 0;

	bool fSuccess = SetCommMask(bluetoothHandle, EV_CTS | EV_DSR);
	if (!fSuccess) {
		bluetoothHandle = INVALID_HANDLE_VALUE;
		connected_ = 0;
	}

	OVERLAPPED o;
	o.hEvent = CreateEvent(
		NULL,   // default security attributes
		TRUE,   // manual-reset event
		FALSE,  // not signaled
		NULL    // no name
		);

	o.Internal = 0;
	o.InternalHigh = 0;
	o.Offset = 0;
	o.OffsetHigh = 0;

	COMMTIMEOUTS CommTimeOuts;
	CommTimeOuts.ReadIntervalTimeout = 3;
	CommTimeOuts.ReadTotalTimeoutMultiplier = 1;
	CommTimeOuts.ReadTotalTimeoutConstant = 2;
	CommTimeOuts.WriteTotalTimeoutMultiplier = 0;
	CommTimeOuts.WriteTotalTimeoutConstant = 0;

	SetCommTimeouts(bluetoothHandle, &CommTimeOuts);

	if (!connected_) {
		std::cout << "Error connecting to EV3" << std::endl;
		system("pause");
		exit(0);
	} else {
		connected = 1;
		std::cout << "Connected to EV3" << std::endl;
		motors(0, 0);
	}
}

void EV3::motors(int leftSpeed, int rightSpeed) {
	if (leftSpeed > 100) leftSpeed = 100;
	if (leftSpeed < -100) leftSpeed = -100;
	if (rightSpeed > 100) rightSpeed = 100;
	if (rightSpeed < -100) rightSpeed = -100;


	outputSpeed(rMotor, rightSpeed);
	outputSpeed(lMotor, leftSpeed);
	outputStart(rMotor | lMotor);
	if (!leftSpeed)
		outputStop(lMotor, STOP_FLOAT);

	if (!rightSpeed)
		outputStop(rMotor, STOP_FLOAT);
}

#include <iostream>

EV3::~EV3() {
	if (connected)
		outputStop(MOTORS::MOTOR_A | MOTORS::MOTOR_B | MOTORS::MOTOR_C | MOTORS::MOTOR_D, EV3::STOP_FLOAT);
//		motors(0, 0);
	Sleep(100);
	CloseHandle(bluetoothHandle);
	Sleep(1000);
}

int EV3::_send(void* data, int packSize) {
	if (bluetoothHandle == INVALID_HANDLE_VALUE) return -1;
	if (packSize == 0)
		return 0;
	DWORD bc;
	do {
		WriteFile(bluetoothHandle, data, packSize, &bc, NULL);
		Sleep(1);
	} while (bc == 0);
	return packSize;
}

int EV3::_recv(void* data, int maxSize) {
	int readed, mustread = 0;

	BOOL tSuccess;
	//tSuccess = PeekNamedPipe(comHandle, NULL, 0, NULL, (LPDWORD)&mustread, NULL) | 1;
	//if (tSuccess) {
	//if (mustread > maxSize) mustread = maxSize;
	mustread = maxSize;
	if (mustread != 0)
		ReadFile(bluetoothHandle, data, mustread, (LPDWORD)&readed, NULL);
	else
		return 0;
	return readed;

	//} else {
	//	return -1;
	//}
}