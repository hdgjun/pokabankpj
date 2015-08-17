/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.entity;

import com.poka.util.StringUtil;

/**
 *
 * @author Administrator
 */
public  class ImageSnoData {

        private byte data[];

        public ImageSnoData() {
            this.data = new byte[128];
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        @Override
        public String toString() {
            String s = "";

            s += StringUtil.byteToHexString(data, 0, 128);

            s += "\n";
            return s;
        }
    }
