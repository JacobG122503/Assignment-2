import java.io.*;
import java.util.HashMap;

public class assignment2 {
    public static HashMap<Integer, String> opcodes = new HashMap<>();

    public static HashMap<Integer, String> conditions = new HashMap<>();

    public static int instructionCount = 1;

    public static void main(String[] args) {
        opcodes.put(0b10001011000, "ADD");
        opcodes.put(0b1001000100, "ADDI");
        opcodes.put(0b10001010000, "AND");
        opcodes.put(0b1001001000, "ANDI");
        opcodes.put(0b000101, "B");
        opcodes.put(0b01010100, "B.");
        opcodes.put(0b100101, "BL");
        opcodes.put(0b11010110000, "BR");
        opcodes.put(0b10110101, "CBNZ");
        opcodes.put(0b10110100, "CBZ");
        opcodes.put(0b11001010000, "EOR");
        opcodes.put(0b1101001000, "EORI");
        opcodes.put(0b11111000010, "LDUR");
        opcodes.put(0b11010011011, "LSL");
        opcodes.put(0b11010011010, "LSR");
        opcodes.put(0b10101010000, "ORR");
        opcodes.put(0b1011001000, "ORRI");
        opcodes.put(0b11111000000, "STUR");
        opcodes.put(0b11001011000, "SUB");
        opcodes.put(0b1101000100, "SUBI");
        opcodes.put(0b1111000100, "SUBIS");
        opcodes.put(0b11101011000, "SUBS");
        opcodes.put(0b10011011000, "MUL");
        opcodes.put(0b11111111101, "PRNT");
        opcodes.put(0b11111111100, "PRNL");
        opcodes.put(0b11111111110, "DUMP");
        opcodes.put(0b11111111111, "HALT");

        conditions.put(0x0, "EQ");
        conditions.put(0x1, "NE");
        conditions.put(0x2, "HS");
        conditions.put(0x3, "LO");
        conditions.put(0x4, "MI");
        conditions.put(0x5, "PL");
        conditions.put(0x6, "VS");
        conditions.put(0x7, "VC");
        conditions.put(0x8, "HI");
        conditions.put(0x9, "LS");
        conditions.put(0xa, "GE");
        conditions.put(0xb, "LT");
        conditions.put(0xc, "GT");
        conditions.put(0xd, "LE");

        try {
            File instructionsFile = new File(args[0]);
            DataInputStream readInstructions = new DataInputStream(new BufferedInputStream(new FileInputStream(instructionsFile)));

            while(readInstructions.available() >= 4){
                byte firstByte = readInstructions.readByte();
                int firstByteInt = (firstByte & 0xFF) << 24;

                byte secondByte = readInstructions.readByte();
                int secondByteInt = (secondByte & 0xFF) << 16;

                byte thirdByte = readInstructions.readByte();
                int thirdByteInt = (thirdByte & 0xFF) << 8;

                byte fourthByte = readInstructions.readByte();
                int fourthByteInt = fourthByte & 0xFF;

                int instruction = firstByteInt + secondByteInt + thirdByteInt + fourthByteInt;

                disassemble(instruction);
            }
        }
        catch (IOException error){
            System.out.println(error);
        }
    }

    public static void disassemble(int instruction){
        String instructionString = "";

        int R_D_opcode = (instruction >> 21) & 0x7FF;
        int I_opcode = (instruction >> 22) & 0x3FF;
        int CB_opcode = (instruction >> 24) & 0xFF;
        int B_opcode = (instruction >> 26) & 0x3F;

        if(opcodes.containsKey(R_D_opcode)){
            instructionString += opcodes.get(R_D_opcode);
            if((R_D_opcode == 0b10001011000) || (R_D_opcode == 0b10001010000) || (R_D_opcode == 0b11001010000) || (R_D_opcode == 0b10101010000) || (R_D_opcode == 0b11001011000) || (R_D_opcode == 0b11101011000) || (R_D_opcode == 0b10011011000)){
                int Rd = instruction & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28){
                    RdString = "SP";
                }
                else if(Rd == 29){
                    RdString = "FP";
                }
                else if(Rd == 30){
                    RdString = "LR";
                }
                else if(Rd == 31){
                    RdString = "XZR";
                }

                int Rn = instruction >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28){
                    RnString = "SP";
                }
                else if(Rn == 29){
                    RnString = "FP";
                }
                else if(Rn == 30){
                    RnString = "LR";
                }
                else if(Rn == 31){
                    RnString = "XZR";
                }

                int Rm = instruction >> 16 & 0x1F;
                String RmString = "X" + Rm;
                if(Rm == 28){
                    RmString = "SP";
                }
                else if(Rm == 29){
                    RmString = "FP";
                }
                else if(Rm == 30){
                    RmString = "LR";
                }
                else if(Rm == 31){
                    RmString = "XZR";
                }

                instructionString += " " + RdString + ", " + RnString + ", " + RmString;
            }

            else if(R_D_opcode == 0b11010110000){
                int Rn = instruction >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28){
                    RnString = "SP";
                }
                else if(Rn == 29){
                    RnString = "FP";
                }
                else if(Rn == 30){
                    RnString = "LR";
                }
                else if(Rn == 31){
                    RnString = "XZR";
                }

                instructionString += " " + RnString;
            }

            else if((R_D_opcode == 0b11010011011) || (R_D_opcode == 0b11010011010)) {
                int Rd = instruction & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28){
                    RdString = "SP";
                }
                else if(Rd == 29){
                    RdString = "FP";
                }
                else if(Rd == 30){
                    RdString = "LR";
                }
                else if(Rd == 31){
                    RdString = "XZR";
                }

                int Rn = instruction >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28){
                    RnString = "SP";
                }
                else if(Rn == 29){
                    RnString = "FP";
                }
                else if(Rn == 30){
                    RnString = "LR";
                }
                else if(Rn == 31){
                    RnString = "XZR";
                }

                int shamt = instruction >> 10 & 0x3F;
                if(shamt >= 32){
                    shamt -= 64;
                }

                instructionString += " " + RdString + ", " + RnString + ", #" + shamt;
            }

            else if(R_D_opcode == 0b11111111101){
                int Rd = instruction & 0x1F;
                String RdString = "X" + Rd;
                if(Rd == 28){
                    RdString = "SP";
                }
                else if(Rd == 29){
                    RdString = "FP";
                }
                else if(Rd == 30){
                    RdString = "LR";
                }
                else if(Rd == 31){
                    RdString = "XZR";
                }

                instructionString += " " + RdString;
            }

            else if((R_D_opcode == 0b11111000010) || (R_D_opcode == 0b11111000000)){
                int Rt = instruction & 0x1F;
                String RtString = "X" + Rt;
                if(Rt == 28){
                    RtString = "SP";
                }
                else if(Rt == 29){
                    RtString = "FP";
                }
                else if(Rt == 30){
                    RtString = "LR";
                }
                else if(Rt == 31){
                    RtString = "XZR";
                }

                int Rn = instruction >> 5 & 0x1F;
                String RnString = "X" + Rn;
                if(Rn == 28){
                    RnString = "SP";
                }
                else if(Rn == 29){
                    RnString = "FP";
                }
                else if(Rn == 30){
                    RnString = "LR";
                }
                else if(Rn == 31){
                    RnString = "XZR";
                }

                int DTAddr = instruction >> 12 & 0x1FF;
                if(DTAddr >= 256){
                    DTAddr -= 512;
                }

                instructionString += " " + RtString + ", [" + RnString + ", #" + DTAddr + "]";
            }

            else{
            }
        }

        else if(opcodes.containsKey(I_opcode)){
            instructionString += opcodes.get(I_opcode);

            int Rd = instruction & 0x1F;
            String RdString = "X" + Rd;
            if(Rd == 28){
                RdString = "SP";
            }
            else if(Rd == 29){
                RdString = "FP";
            }
            else if(Rd == 30){
                RdString = "LR";
            }
            else if(Rd == 31) {
                RdString = "XZR";
            }

            int Rn = instruction >> 5 & 0x1F;
            String RnString = "X" + Rn;
            if(Rn == 28){
                RnString = "SP";
            }
            else if(Rn == 29){
                RnString = "FP";
            }
            else if(Rn == 30){
                RnString = "LR";
            }
            else if(Rn == 31){
                RnString = "XZR";
            }

            int ALUImm = instruction >> 10 & 0xFFF;
            if(ALUImm >= 2048){
                ALUImm -= 4096;
            }

            instructionString += " " + RdString + ", " + RnString + ", #" + ALUImm;
        }

        else if(opcodes.containsKey(CB_opcode)){
            instructionString += opcodes.get(CB_opcode);

            if(CB_opcode == 0b01010100){
                int cond = instruction & 0x1F;
                String condString = conditions.get(cond);
                instructionString += condString;
            }

            int BranchAddr = instruction >> 5 & 0x7FFFF;
            if(BranchAddr >= 262144){
                BranchAddr -= 524288;
            }

            instructionString += " L" + (instructionCount + BranchAddr);
        }

        else if(opcodes.containsKey(B_opcode)){
            int BranchAddr = instruction & 0x3FFFFFF;
            if(BranchAddr >= 33554432){
                BranchAddr -= 67108864;
            }

            instructionString += opcodes.get(B_opcode) + " L" + (instructionCount + BranchAddr);
        }

        else{
            System.out.println("Opcode not found --> Error with program");
        }

        System.out.println("L" + instructionCount + ": " + instructionString);
        instructionCount++;
    }
}