/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.entity;

/**
 *
 * @author Administrator
 */
public class RGBQUAD {

        private int rgbBlue;//蓝色的亮度（值范围为0-255）
        private int rgbGreen;//绿色的亮度（值范围为0-255）
        private int rgbRed;//红色的亮度（值范围为0-255）
        private final int rgbReserved = 0;//保留，必须为0

        public RGBQUAD(int rgbBlue,int rgbGreen,int rgbRed){
            this.rgbBlue = rgbBlue;
            this.rgbGreen = rgbGreen;
            this.rgbRed = rgbRed;
        }
        /**
         * @return the rgbBlue
         */
        public int getRgbBlue() {
            return rgbBlue;
        }

        /**
         * @param rgbBlue the rgbBlue to set
         */
        public void setRgbBlue(int rgbBlue) {
            this.rgbBlue = rgbBlue;
        }

        /**
         * @return the rgbGreen
         */
        public int getRgbGreen() {
            return rgbGreen;
        }

        /**
         * @param rgbGreen the rgbGreen to set
         */
        public void setRgbGreen(int rgbGreen) {
            this.rgbGreen = rgbGreen;
        }

        /**
         * @return the rgbRed
         */
        public int getRgbRed() {
            return rgbRed;
        }

        /**
         * @param rgbRed the rgbRed to set
         */
        public void setRgbRed(int rgbRed) {
            this.rgbRed = rgbRed;
        }

        /**
         * @return the rgbReserved
         */
        public int getRgbReserved() {
            return rgbReserved;
        }

    }