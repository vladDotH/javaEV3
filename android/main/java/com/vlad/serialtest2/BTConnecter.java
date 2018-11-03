package c.vlad.serialtest2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BTConnecter {
    private static final String TAG = "CONNECTER";

    private BluetoothAdapter adapter = null;
    private BluetoothDevice device = null;

    private BluetoothSocket socket = null;
    private OutputStream writer = null;

    static final UUID ID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    protected BTConnecter() {
        Log.d(TAG, "Connecter launched");
    }

    protected void connect(String name) {
        Log.d(TAG, "method connect: starting");

        try {
            adapter = BluetoothAdapter.getDefaultAdapter();

            Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice _device : pairedDevices) {
                    if (_device.getName().equals(name)) {
                        device = _device;
                        Log.d(TAG, "connect: device found : " + _device.getName());
                        break;
                    }
                }
            }

        } catch (Exception e) {
            Log.d(TAG, "connect err" + e.getMessage());
        }

        if (device == null) {
            Log.d(TAG, "connect: device not found ");
            return;
        }

        adapter = BluetoothAdapter.getDefaultAdapter();                         //get the mobile bluetooth device

        try {
            Log.d(TAG, "connect: creating RFCOMM connection");

            socket = device.createInsecureRfcommSocketToServiceRecord(ID);
        } catch (IOException e) {
            Log.d(TAG, "connect: err " + e.getMessage());
        }

        try {
            Log.d(TAG, "connecting...");

            socket.connect();
        } catch (IOException e) {
            Log.d(TAG, "connect: err " + e.getMessage());

            close();

            Log.d(TAG, "connect: Could not connect to " + device.getName());
            return;
        }

        Log.d(TAG, "connected to " + device.getName() + " < on adress -> " + device.getAddress());

        try {
            Log.d(TAG, "connect: getting outStream on " + device.getName());

            writer = socket.getOutputStream();

            Log.d(TAG, "connect: stream got :" + device.getName());
        } catch (IOException e) {
            Log.d(TAG, "connect: err :" + e.getMessage());
        }
    }

    protected void write(byte[] message) {
        try {
            Log.d(TAG, "write : start");

            writer.write(message);

            Log.d(TAG, "message sent");
        } catch (IOException e) {
            Log.d(TAG, "write err " + e.getMessage());
        }
    }

    protected void close() {
        try {
            Log.d(TAG, "closing socket");

            socket.close();
        } catch (IOException e) {
            Log.d(TAG, "closing err " + e.getMessage());
        }
    }
}
