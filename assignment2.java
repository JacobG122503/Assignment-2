/*
 * jacobgar@iastate.edu
 * Assignment 2
 * 12/6/24
 * 
 * 
 * To build: ./build.sh
 * To run: ./run.sh assignment1.legv8asm.machine
 */

import java.io.*;
import java.util.*;

public class assignment2 {
    public static HashMap<Integer, String> conds = new HashMap<>();
    public static HashMap<Integer, String> opcodes = new HashMap<>();
    
    public static int instCount = 1;

    public static void main(String[] args) {
        opcodes.put(0b000101, "B");
        opcodes.put(0b01010100, "B.");
        opcodes.put(0b100101, "BL");
        opcodes.put(0b1001000100, "ADDI");
        opcodes.put(0b1001001000, "ANDI");
        opcodes.put(0b10001010000, "AND");
        opcodes.put(0b10001011000, "ADD");
        opcodes.put(0b10011011000, "MUL");
        opcodes.put(0b10101010000, "ORR");
        opcodes.put(0b1011001000, "ORRI");
        opcodes.put(0b10110100, "CBZ");
        opcodes.put(0b10110101, "CBNZ");
        opcodes.put(0b11001010000, "EOR");
        opcodes.put(0b11001011000, "SUB");
        opcodes.put(0b1101000100, "SUBI");
        opcodes.put(0b1101001000, "EORI");
        opcodes.put(0b11010011010, "LSR");
        opcodes.put(0b11010011011, "LSL");
        opcodes.put(0b11010110000, "BR");
        opcodes.put(0b11101011000, "SUBS");
        opcodes.put(0b1111000100, "SUBIS");
        opcodes.put(0b11111000000, "STUR");
        opcodes.put(0b11111000010, "LDUR");
        opcodes.put(0b11111111100, "PRNL");
        opcodes.put(0b11111111101, "PRNT");
        opcodes.put(0b11111111110, "DUMP");
        opcodes.put(0b11111111111, "HALT");

        conds.put(0x0, "EQ");
        conds.put(0x1, "NE");
        conds.put(0x2, "HS");
        conds.put(0x3, "LO");
        conds.put(0x4, "MI");
        conds.put(0x5, "PL");
        conds.put(0x6, "VS");
        conds.put(0x7, "VC");
        conds.put(0x8, "HI");
        conds.put(0x9, "LS");
        conds.put(0xa, "GE");
        conds.put(0xb, "LT");
        conds.put(0xc, "GT");
        conds.put(0xd, "LE");

        try {
            File file = new File(args[0]);
            DataInputStream fileInsts = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

            while(fileInsts.available() >= 4) {
                byte firstByte = fileInsts.readByte();
                int firstInt = (firstByte & 0xFF) << 24;
                byte secondByte = fileInsts.readByte();
                int secondInt = (secondByte & 0xFF) << 16;
                byte thirdByte = fileInsts.readByte();
                int thirdInt = (thirdByte & 0xFF) << 8;
                byte fourthByte = fileInsts.readByte();
                int fourthInt = fourthByte & 0xFF;
                int inst = firstInt + secondInt + thirdInt + fourthInt;

                disassemble(inst);
            }
            if (fileInsts != null) {
                fileInsts.close();
            }
        }
        catch (IOException error) {
            System.out.println(error);
        }
    }

    public static void disassemble(int inst) {
        String instString = "";

        int RDcode = (inst >> 21) & 0x7FF;
        int Icode = (inst >> 22) & 0x3FF;
        int CBcode = (inst >> 24) & 0xFF;
        int Bcode = (inst >> 26) & 0x3F;

        if(opcodes.containsKey(RDcode)) {
            instString += opcodes.get(RDcode);
            if((RDcode == 0b10001011000) ||
            (RDcode == 0b10001010000) ||
            (RDcode == 0b11001010000) ||
            (RDcode == 0b10101010000) ||
            (RDcode == 0b11001011000) ||
            (RDcode == 0b11101011000) ||
            (RDcode == 0b10011011000)) {
                int Rd = inst & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28) {
                    RdString = "SP";
                }
                else if(Rd == 29) {
                    RdString = "FP";
                }
                else if(Rd == 30) {
                    RdString = "LR";
                }
                else if(Rd == 31) {
                    RdString = "XZR";
                }

                int Rn = inst >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28) {
                    RnString = "SP";
                }
                else if(Rn == 29) {
                    RnString = "FP";
                }
                else if(Rn == 30) {
                    RnString = "LR";
                }
                else if(Rn == 31) {
                    RnString = "XZR";
                }

                int Rm = inst >> 16 & 0x1F;
                String RmString = "X" + Rm;
                if(Rm == 28) {
                    RmString = "SP";
                }
                else if(Rm == 29) {
                    RmString = "FP";
                }
                else if(Rm == 30) {
                    RmString = "LR";
                }
                else if(Rm == 31) {
                    RmString = "XZR";
                }

                instString += " " + RdString + ", " + RnString + ", " + RmString;
            } else if(RDcode == 0b11010110000) {
                int Rn = inst >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28) {
                    RnString = "SP";
                }
                else if(Rn == 29) {
                    RnString = "FP";
                }
                else if(Rn == 30) {
                    RnString = "LR";
                }
                else if(Rn == 31) {
                    RnString = "XZR";
                }

                instString += " " + RnString;
            } else if((RDcode == 0b11010011011) || (RDcode == 0b11010011010)) {
                int Rd = inst & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28) {
                    RdString = "SP";
                }
                else if(Rd == 29) {
                    RdString = "FP";
                }
                else if(Rd == 30) {
                    RdString = "LR";
                }
                else if(Rd == 31) {
                    RdString = "XZR";
                }

                int Rn = inst >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28) {
                    RnString = "SP";
                }
                else if(Rn == 29) {
                    RnString = "FP";
                }
                else if(Rn == 30) {
                    RnString = "LR";
                }
                else if(Rn == 31) {
                    RnString = "XZR";
                }

                int shamt = inst >> 10 & 0x3F;
                if(shamt >= 32) {
                    shamt -= 64;
                }

                instString += " " + RdString + ", " + RnString + ", #" + shamt;
            } else if(RDcode == 0b11111111101) {
                int Rd = inst & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28) {
                    RdString = "SP";
                }
                else if(Rd == 29) {
                    RdString = "FP";
                }
                else if(Rd == 30) {
                    RdString = "LR";
                }
                else if(Rd == 31) {
                    RdString = "XZR";
                }

                instString += " " + RdString;
            } else if((RDcode == 0b11111000010) || (RDcode == 0b11111000000)) {
                int Rt = inst & 0x1F;
                String RtString = "X" + Rt;
                if(Rt == 28) {
                    RtString = "SP";
                }
                else if(Rt == 29) {
                    RtString = "FP";
                }
                else if(Rt == 30) {
                    RtString = "LR";
                }
                else if(Rt == 31) {
                    RtString = "XZR";
                }

                int Rn = inst >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28) {
                    RnString = "SP";
                }
                else if(Rn == 29) {
                    RnString = "FP";
                }
                else if(Rn == 30) {
                    RnString = "LR";
                }
                else if(Rn == 31) {
                    RnString = "XZR";
                }

                int DTAddr = inst >> 12 & 0x1FF;
                if(DTAddr >= 256) {
                    DTAddr -= 512;
                }

                instString += " " + RtString + ", [" + RnString + ", #" + DTAddr + "]";
            }
        } else if(opcodes.containsKey(Icode)) {
            instString += opcodes.get(Icode);

            int Rd = inst & 0x1F;
            String RdString = "X" + Rd;
            if(Rd == 28) {
                RdString = "SP";
            } else if(Rd == 29) {
                RdString = "FP";
            } else if(Rd == 30) {
                RdString = "LR";
            } else if(Rd == 31) {
                RdString = "XZR";
            }

            int Rn = inst >> 5 & 0x1F;
            String RnString = "X" + Rn;
            if(Rn == 28) {
                RnString = "SP";
            } else if(Rn == 29) {
                RnString = "FP";
            } else if(Rn == 30) {
                RnString = "LR";
            } else if(Rn == 31) {
                RnString = "XZR";
            }

            int ALUImm = inst >> 10 & 0xFFF;
            if(ALUImm >= 2048) {
                ALUImm -= 4096;
            }

            instString += " " + RdString + ", " + RnString + ", #" + ALUImm;
        } else if(opcodes.containsKey(CBcode)) {
            instString += opcodes.get(CBcode);

            if(CBcode == 0b01010100) {
                int cond = inst & 0x1F;
                String condString = conds.get(cond);
                instString += condString;
            }

            int bAddr = inst >> 5 & 0x7FFFF;
            if(bAddr >= 262144) {
                bAddr -= 524288;
            }

            instString += " label_" + (instCount + bAddr);
        } else if(opcodes.containsKey(Bcode)) {
            int bAddr = inst & 0x3FFFFFF;
            if(bAddr >= 33554432) {
                bAddr -= 67108864;
            }

            instString += opcodes.get(Bcode) + " label_" + (instCount + bAddr);
        } else{
            System.out.println("ERROR: Could not find opcode.");
        }

        System.out.println("label_" + instCount + ": " + instString);
        instCount++;
    }
}