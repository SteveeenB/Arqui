package Modelo;

public class RepresentacionIEEE {

    public double dec(double num) {
        int exp = 0;
        while (num >= 2) {
            num /= 2;
            exp++;
        }
        return num;
    }

    public String decABin(double num, int size) {
        if (num >= 1) {
            num -= 1;
        }
        StringBuilder bin = new StringBuilder();
        for (int i = 0; i < size; i++) {
            num *= 2;
            if (num >= 1) {
                bin.append('1');
                num -= 1;
            } else {
                bin.append('0');
            }
        }
        return bin.toString();
    }

    public String expABin(int exp, int bits) {
        String bin = Integer.toBinaryString(exp);
        if (bin.length() > bits) {
            return bin.substring(0, bits);
        } else {
            return String.format("%" + bits + "s", bin).replace(' ', '0');
        }
    }

    public double IEEEToDec64(String ieee) {
        if (ieee.length() != 64) {
            throw new IllegalArgumentException("El número IEEE debe tener 64 bits.");
        }

   
        int signo = (ieee.charAt(0) == '0') ? 1 : -1;

        String exponenteStr = ieee.substring(1, 12);
        String mantisaStr = ieee.substring(12);

        int exponente = Integer.parseInt(exponenteStr, 2);
        long mantisaBits = Long.parseLong(mantisaStr, 2);

        double valor;

        if (exponente == 0) {
            valor = signo * mantisaBits * Math.pow(2, -52) * Math.pow(2, -1022);
        } else if (exponente == 0x7FF) { 
            
            valor = (mantisaBits == 0) ? signo * Double.POSITIVE_INFINITY : Double.NaN;
        } else {
            
            int expReal = exponente - 1023;
            double mantisa = 1.0 + mantisaBits * Math.pow(2, -52);
            valor = signo * mantisa * Math.pow(2, expReal);
        }

        return valor;
    }

    public double IEEEToDec32(String ieee) {
        if (ieee.length() != 32) {
            throw new IllegalArgumentException("El número IEEE debe tener 32 bits.");
        }

        int signo = (ieee.charAt(0) == '0') ? 1 : -1;

        String exponenteStr = ieee.substring(1, 9);
        int exponente = Integer.parseInt(exponenteStr, 2);

        String mantisaStr = ieee.substring(9);
        double mantisa = 1.0;

        if (exponente == 0) {
            mantisa = 0.0;
            exponente = -126;
        } else if (exponente == 0xFF) {
            return signo * (mantisa + 1.0) * Double.POSITIVE_INFINITY;
        } else {
            exponente -= 127;
        }

        for (int i = 0; i < mantisaStr.length(); i++) {
            if (mantisaStr.charAt(i) == '1') {
                mantisa += Math.pow(2, -(i + 1));
            }
        }

        return signo * mantisa * Math.pow(2, exponente);
    }

    public String decToIEEE32(double num) {
        if (num == 0) {
            return "00000000000000000000000000000000";
        }

        StringBuilder ieee = new StringBuilder();
        if (num < 0) {
            ieee.append('1');
            num = -num;
        } else {
            ieee.append('0');
        }

        int exp = (int) Math.floor(Math.log(num) / Math.log(2));
        int biasedExp = exp + 127;

        if (biasedExp <= 0) {
            ieee.append("00000000");
            double mantisaSubnormal = num * Math.pow(2, 126 + 23);
            int intMantisa = (int) Math.round(mantisaSubnormal);
            String mantisaBits = Integer.toBinaryString(intMantisa);
            mantisaBits = String.format("%23s", mantisaBits).replace(' ', '0');
            if (mantisaBits.length() > 23) {
                mantisaBits = mantisaBits.substring(0, 23);
            }
            ieee.append(mantisaBits);
        } else {
            ieee.append(expABin(biasedExp, 8));
            double mantisa = num / Math.pow(2, exp);
            ieee.append(decABin(mantisa, 23));
        }

        while (ieee.length() < 32) {
            ieee.append('0');
        }

        return ieee.substring(0, 32);
    }

    public String decToIEEE64(double num) {
        if (num == 0) {
            return "0000000000000000000000000000000000000000000000000000000000000000";
        }

        StringBuilder ieee = new StringBuilder();
        if (num < 0) {
            ieee.append('1');
            num = -num;
        } else {
            ieee.append('0');
        }

        int exp = (int) Math.floor(Math.log(num) / Math.log(2));
        int biasedExp = exp + 1023;

        if (biasedExp <= 0) {
            ieee.append("00000000000");
            double mantisaSubnormal = num * Math.pow(2, 1022 + 52);
            long longMantisa = (long) mantisaSubnormal;
            String mantisaBits = Long.toBinaryString(longMantisa);
            mantisaBits = String.format("%52s", mantisaBits).replace(' ', '0');
            if (mantisaBits.length() > 52) {
                mantisaBits = mantisaBits.substring(0, 52);
            }
            ieee.append(mantisaBits);
        } else {
            ieee.append(expABin(biasedExp, 11));
            double mantisa = num / Math.pow(2, exp);
            ieee.append(decABin(mantisa, 52));
        }

        while (ieee.length() < 64) {
            ieee.append('0');
        }

        return ieee.substring(0, 64);
    }
}
