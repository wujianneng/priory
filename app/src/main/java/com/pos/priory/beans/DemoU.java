package com.pos.priory.beans;

import java.io.Serializable;

/**
 * Created by Bruce_Chiang on 2017/3/15.
 */

public class DemoU implements Serializable {
    private String pc;
    private String epc;
    private String crc16;
    private String count;
    private String percentage;

    public DemoU(String pc, String epc, String crc, String c, String per) {
        this.pc = pc;
        this.epc = epc;
        this.crc16 = crc;
        this.count = c;
        this.percentage = per;
    }

    public DemoU(String pc, String epc, String crc) {
        this.pc = pc;
        this.epc = epc;
        this.crc16 = crc;
    }

    public void PC(String pc) { this.pc = pc; }
    public void EPC(String epc) { this.epc = epc; }
    public void CRC16(String crc) { this.crc16 = crc; }
    public void Count(String c) { this.count = c; }
    public void Percentage(String per) { this.percentage = per;}

    public String PC() { return this.pc; }
    public String EPC() { return this.epc; }
    public String CRC16() { return this.crc16; }
    public String Count() { return this.count; }
    public String Percentage() { return this.percentage; }
}
