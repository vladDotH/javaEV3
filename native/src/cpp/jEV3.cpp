#include "jEV3.h"
#include "EV3.hpp"

/*
 * Class:     jEV3
 * Method:    _connect
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_jEV3__1connect
(JNIEnv *, jobject, jint port) {
	EV3 *bot = new EV3(EV3::MOTOR_A, EV3::MOTOR_B);
	std::cout << "Try to connect " << port << std::endl;
	bot->connect( (int)port );

	return reinterpret_cast<long long int>(bot);
}

/*
 * Class:     jEV3
 * Method:    _move
 * Signature: (BIJ)V
 */
JNIEXPORT void JNICALL Java_jEV3__1move
(JNIEnv *, jobject, jbyte motor, jint speed, jlong ptr) {
	reinterpret_cast<EV3*>(ptr)->outputSpeed(motor, speed);
	reinterpret_cast<EV3*>(ptr)->outputStart(motor);
}

/*
 * Class:     jEV3
 * Method:    _stop
 * Signature: (BIJ)V
 */
JNIEXPORT void JNICALL Java_jEV3__1stop
(JNIEnv *, jobject, jbyte motor, jint type, jlong ptr) {
	reinterpret_cast<EV3*>(ptr)->outputStop(motor, type);
}