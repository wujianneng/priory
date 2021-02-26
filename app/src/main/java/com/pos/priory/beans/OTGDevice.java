package com.pos.priory.beans;

import android.hardware.usb.UsbDevice;

import com.pos.priory.services.ReaderService;


/**
 * Created by Bruce_Chiang on 2017/3/17.
 */

public class OTGDevice {
    private int imageId;
    private UsbDevice usbDevice;
    private String deviceName;
    private String manufacturerName;
    private String productName;
    private String productId;
    private String vendorId;

    public OTGDevice(UsbDevice dev)
    {
        this.usbDevice = dev;
        this.deviceName = dev.getDeviceName();
        this.manufacturerName = dev.getManufacturerName();
        this.productName = dev.getProductName();
        this.productId = ReaderService.Format.makesUpZero(Integer.toString(dev.getProductId(), 16), 4);
        this.vendorId = ReaderService.Format.makesUpZero(Integer.toString(dev.getVendorId(), 16), 4);
    }

    public void setImage(int id) { this.imageId = id; }

    public int getImage() { return this.imageId; }

    public UsbDevice getUsbDevice() { return usbDevice; }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getManufacturerName() {
        return this.manufacturerName;
    }

    public String getProductName() {
        return this.productName;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getVendorId() {
        return this.vendorId;
    }
}
