import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

public class GeneratorKoda {
    private static final int VALID_CONST_LOWER_BOUND = -(1 << 19);
    private static final int VALID_CONST_UPPER_BOUND = (1 << 19) - 1;

    private static int logIliIzrazCnt = 0;
    private static int logIIzrazCnt = 0;

    private static StringBuilder kodFunkcija = new StringBuilder();
    private static StringBuilder globalneInicijalizacije = new StringBuilder();

    public static void main(String[] args) throws IOException {
        SemantickiAnalizator.main(null);

        String friscCode = getFinalCode();

        Files.write(Paths.get("./a.frisc"), friscCode.toString().getBytes());
    }

    public static String getFinalCode() {
        StringBuilder sb = new StringBuilder();

        sb.append(" MOVE 40000, R7\n")
                .append(getGlobalneInicijalizacije())
                .append(" CALL main\n HALT\n")
                .append(defineGlobalVariables())
                .append(defineConstants())
                .append(getKodFunkcija())
                .append(generateUtilProcedures());

        return sb.toString();
    }

    private static String defineGlobalVariables() {
        StringBuilder sb = new StringBuilder(" ;globalne varijable\n");
        Collection<Varijabla> vars = TablicaZnakova.getGlobalniDjelokrug().getVarijable();

        for(Varijabla var : vars){
            GloblnaVarijabla v = (GloblnaVarijabla)var;
            sb.append(defineGlobalVariable(v.getLabela(), v.getBrElem()));
        }

        return sb.toString();
    }

    private static String defineGlobalVariable(String labela, int brElem) {
        StringBuilder sbFill = new StringBuilder("0");

        for(int i = 0; i < brElem - 1; ++i) {
            sbFill.append(", 0");
        }

        return labela + " DW " + sbFill.toString() + "\n";
    }

    private static String defineConstants() {
        StringBuilder sb = new StringBuilder(" ;konstante\n");

        for(Map.Entry<Integer, String> e : TablicaKonstanti.getKonstanteInt().entrySet()) {
            sb.append(defineConstant(e.getValue(), e.getKey()));
        }

        for(Map.Entry<String, String> e : TablicaKonstanti.getKonstanteString().entrySet()) {
            sb.append(defineConstant(e.getValue(), e.getKey()));
        }

        return sb.toString();
    }

    private static String defineConstant(String labela, int k) {
        return labela + " DW %D " + k + "\n";
    }

    private static String defineConstant(String labela, String k) {
        char[] arr = k.toCharArray();
        StringBuilder sb = new StringBuilder("%D " + (int)arr[0]);

        for(int i = 1; i < arr.length; ++i) {
            sb.append(", %D " + (int)arr[i]);
        }

        return labela + " DW " + sb.toString() + "\n";
    }

    public static void addFunctionCode(String code) {
        kodFunkcija.append(code);
    }

    public static void addGlobalDeclaration(String code) {
        globalneInicijalizacije.append(code);
    }

    public static String getKodFunkcija() {
        return " ;kod funkcija\n" + kodFunkcija.toString();
    }

    public static String getGlobalneInicijalizacije() {
        return " ;inicijalizacija globalnih varijabli\n" +globalneInicijalizacije.toString();
    }

    public static String reserveMemoryOnStack(int n) {
        return " SUB R7, %D " + n + ", R7\n";
    }

    public static String freeMemoryOnStack(int n) {
        return " ADD R7, %D " + n + ", R7\n";
    }

    public static String popStack(String register) {
        return " POP " + register + "\n";
    }

    public static String pushRegister(String register) {

        if(register.equals("R7")) {
            return " MOVE R7, R0\n" +
                    " PUSH R0\n";
        }

        return " PUSH " + register + "\n";
    }

    public static String pushStack(String register) {
        return " PUSH " + register + "\n";
    }

    public static String logIliIzraz(String a, String b) {
        StringBuilder sb = new StringBuilder();
        String labelaTrue = "LOG_ILI_TRUE_" + logIliIzrazCnt;
        String labelaOpEnd = "LOG_ILI_OPEND_" + logIliIzrazCnt++;

        sb.append(a).append(" POP R1\n" +
                            " ADD R1, 0, R1\n" +
                            " JR_NZ " + labelaTrue +"\n");

        sb.append(b).append(" POP R1\n" +
                            " ADD R1, 0, R1\n" +
                            " JR_NZ " + labelaTrue +"\n");
        sb.append(  " MOVE 0, R1\n" +
                    " JR "+labelaOpEnd+"\n" +
                    labelaTrue + " MOVE 1, R1\n" +
                    labelaOpEnd + " PUSH R1\n");

        return  sb.toString();
    }

    public static String logIIzraz(String a, String b) {
        StringBuilder sb = new StringBuilder();
        String labelaFalse = "LOG_I_FALSE_" + logIIzrazCnt;
        String labelaOpEnd = "LOG_I_OPEND_" + logIIzrazCnt++;

        sb.append(a).append(" POP R1\n" +
                            " ADD R1, 0, R1\n" +
                            " JR_Z " + labelaFalse +"\n");

        sb.append(b).append(" POP R1\n" +
                            " ADD R1, 0, R1\n" +
                            " JR_Z " + labelaFalse +"\n");
        sb.append(  " MOVE 1, R1\n" +
                " JR "+labelaOpEnd+"\n" +
                labelaFalse + " MOVE 0, R1\n" +
                labelaOpEnd + " PUSH R1\n");

        return  sb.toString();
    }

    public static String opBinIli() {
        return  " POP R1\n" +
                " POP R2\n" +
                " OR R1, R2, R2\n" +
                " PUSH R2\n";
    }

    public static String opBinXIli() {
        return  " POP R1\n" +
                " POP R2\n" +
                " XOR R1, R2, R2\n" +
                " PUSH R2\n";
    }

    public static String opBinI() {
        return  " POP R1\n" +
                " POP R2\n" +
                " AND R1, R2, R2\n" +
                " PUSH R2\n";
    }

    public static String opAdd() {
        return  " POP R2\n" +
                " POP R1\n" +
                " ADD R1, R2, R2\n" +
                " PUSH R2\n";
    }

    public static String opSub() {
        return  " POP R2\n" +
                " POP R1\n" +
                " SUB R1, R2, R2\n" +
                " PUSH R2\n";
    }

    public static String opEq() {
        return  " CALL OP_EQ\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opNe() {
        return  " CALL OP_NE\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opGT() {
        return  " CALL OP_GT\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opGTE() {
        return  " CALL OP_GTE\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opLT() {
        return  " CALL OP_LT\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opLTE() {
        return  " CALL OP_LTE\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opMUL() {
        return  " CALL OP_MUL\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opUnNEG() {
        return  " CALL OP_NEG\n" +
                " ADD R7, 4, R7 \n" +
                " PUSH R6\n";
    }

    public static String opDIV() {
        return  " CALL OP_DIV\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opMOD() {
        return  " CALL OP_MOD\n" +
                " ADD R7, 8, R7 \n" +
                " PUSH R6\n";
    }

    public static String opPostINC() {
        return " POP R0\n" +
                " LOAD R1, (R0)\n" +
                " PUSH R1\n" +
                " ADD R1, 1, R1\n" +
                " STORE R1, (R0)\n";
    }

    public static String opPostDEC() {
        return " POP R0\n" +
                " LOAD R1, (R0)\n" +
                " PUSH R1\n" +
                " SUB R1, 1, R1\n" +
                " STORE R1, (R0)\n";
    }

    public static String opPreINC() {
        return " POP R0\n" +
                " LOAD R1, (R0)\n" +
                " ADD R1, 1, R1\n" +
                " PUSH R1\n" +
                " STORE R1, (R0)\n";
    }

    public static String opPreDEC() {
        return " POP R0\n" +
                " LOAD R1, (R0)\n" +
                " SUB R1, 1, R1\n" +
                " PUSH R1\n" +
                " STORE R1, (R0)\n";
    }

    public static String opUnMINUS() {
        return " POP R0\n" +
                " XOR R0, -1, R0\n" +
                " ADD R0, 1, R0\n" +
                " PUSH R0\n";
    }

    public static String opUnTILDA() {
        return " POP R0\n" +
                " XOR R0, -1, R0\n" +
                " PUSH R0\n";
    }

    public static String varAssign() {
        return " POP R1\n" +
                " POP R0\n" +
                " STORE R1, (R0)\n";
    }

    public static String dereferenceAddresse() {
        return " POP R0\n" +
                " LOAD R0, (R0)\n" +
                " PUSH R0\n";
    }

    public static String pushStackPointerOfsset(int offset) {
        return " ADD R7, %D " + offset + ", R0\n" +
                " PUSH R0\n";
    }

    public static String copyArray() {
        return  " CALL COPY_ARRAY\n" +
                " ADD R7, %D 12, R7 \n";
    }

    private static boolean isValidConst(int k) {
        return k >= VALID_CONST_LOWER_BOUND && k <= VALID_CONST_UPPER_BOUND;
    }

    public static String pushConstant(int k) {
        if(isValidConst(k)) {
            return " MOVE %D " + k + ", R0\n" +
                    " PUSH R0\n";
        } else {
            String labela = TablicaKonstanti.getLabelaKonstante(k);
            return " LOAD R0, ("+labela+")\n" +
                    " PUSH R0\n";
        }
    }

    public static String pushStringReference(String k) {
        String labela = TablicaKonstanti.getLabelaKonstante(k);
        return " MOVE " + labela + ", R0\n" +
                " PUSH R0\n";
    }

    public static String getVarReference(String idn, Djelokrug trenutni) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;

        Djelokrug izvorni = trenutni;
        Varijabla var = trenutni.getVarijabla(idn);
        while(var == null) {
            trenutni = trenutni.getParent();
            var = trenutni.getVarijabla(idn);
            cnt++;
        }

        if(trenutni.getOznaka() == Djelokrug.Oznaka.GLOBALNI_DJELOKRUG) {
            sb.append(getGlobalVarReference(idn));
            return sb.toString();
        }

        if(trenutni == izvorni) {
            sb.append(getScopeReference(idn, trenutni, "R5"));
            if(var instanceof ParamaterVarijabla && TipoviUtility.isArray(var.getType())) sb.append(dereferenceAddresse());
            return sb.toString();
        }

        sb.append(" MOVE R5, R4\n");
        sb.append(moveUpStackContext(cnt, izvorni));
        sb.append(getScopeReference(idn, trenutni, "R4"));

        if(var instanceof ParamaterVarijabla && TipoviUtility.isArray(var.getType())) sb.append(dereferenceAddresse());

        return sb.toString();
    }

    private static String getGlobalVarReference(String idn) {
        String labela = ((GlobalniParametar)TablicaZnakova.getGlobalniDjelokrug().getVarijabla(idn)).getLabela();
        return " MOVE " + labela + ", R0\n"+
                " PUSH R0\n";
    }

    private static String getScopeReference(String idn, Djelokrug d, String registarVrhaStoga) {
        int offset = ((StogParametar)d.getVarijabla(idn)).getOffsetOdVrha();
        return " ADD " + registarVrhaStoga + ", %D " + offset + ", R0\n" +
                " PUSH R0\n";
    }

    private static String moveUpStackContext(int n, Djelokrug trenutni) {
        StringBuilder sb = new StringBuilder("");
        for(int i = 0; i < n; ++i) {
            int offset = trenutni.getVelicina();
            sb.append(" LOAD R4, (R4 + %D " + offset + ")\n");
            trenutni = trenutni.getParent();
        }
        return sb.toString();
    }

    public static String functionCall(String idn) {
        PrototipFunkcije f = TablicaZnakova.getDefiniranaFunkcija(idn);
        String tip = f.getReturnType();

        StringBuilder sb = new StringBuilder(voidfunctionCall(idn));
        if(!tip.equals("void")) sb.append(" PUSH R6\n");

        return sb.toString();
    }

    private static String voidfunctionCall(String idn) {
        PrototipFunkcije f = TablicaZnakova.getDefiniranaFunkcija(idn);
        int n = f.getPareterTypes().size()*4;

        return " CALL " + idn + "\n" +
                " ADD R7, %D " + n + ", R7\n";
    }



    public static String pushScope(Djelokrug d) {
        StringBuilder sb = new StringBuilder();

        sb.append(GeneratorKoda.pushStack("R5"))
                .append(GeneratorKoda.reserveMemoryOnStack(d.getVelicina()))
                .append(" MOVE R7, R5\n");

        return sb.toString();
    }

    public static  String popScope(Djelokrug d) {
        StringBuilder sb = new StringBuilder();

        sb.append(" MOVE R5, R7\n")
                .append(GeneratorKoda.freeMemoryOnStack(d.getVelicina()))
                .append(GeneratorKoda.popStack("R5"));

        return sb.toString();
    }

    public static String generateUtilProcedures() {
        StringBuilder sb = new StringBuilder();

        //jednakosni izraz
        sb.append("OP_EQ LOAD R1, (R7 + 8)\n" +
                " LOAD R2, (R7 + 4)\n" +
                " CMP R1, R2\n" +
                " JR_EQ OP_EQ_TRUE\n" +
                " MOVE 0, R6\n" +
                " JR OP_EQ_RET\n" +
                "OP_EQ_TRUE  MOVE 1, R6\n" +
                "OP_EQ_RET RET\n");
        //nejednakosni izraz
        sb.append("OP_NE LOAD R1, (R7 + 8)\n" +
                " LOAD R2, (R7 + 4)\n" +
                " CMP R1, R2\n" +
                " JR_NE OP_NE_TRUE\n" +
                " MOVE 0, R6\n" +
                " JR OP_NE_RET\n" +
                "OP_NE_TRUE  MOVE 1, R6\n" +
                "OP_NE_RET RET\n");
        //veci od
        sb.append("OP_GT LOAD R1, (R7 + 8)\n" +
                " LOAD R2, (R7 + 4)\n" +
                " CMP R1, R2\n" +
                " JR_SGT OP_GT_TRUE\n" +
                " MOVE 0, R6\n" +
                " JR OP_GT_RET\n" +
                "OP_GT_TRUE  MOVE 1, R6\n" +
                "OP_GT_RET RET\n");
        //veci ili jednak od
        sb.append("OP_GTE LOAD R1, (R7 + 8)\n" +
                " LOAD R2, (R7 + 4)\n" +
                " CMP R1, R2\n" +
                " JR_SGE OP_GTE_TRUE\n" +
                " MOVE 0, R6\n" +
                " JR OP_GTE_RET\n" +
                "OP_GTE_TRUE  MOVE 1, R6\n" +
                "OP_GTE_RET RET\n");
        //manji od
        sb.append("OP_LT LOAD R1, (R7 + 8)\n" +
                " LOAD R2, (R7 + 4)\n" +
                " CMP R1, R2\n" +
                " JR_SLT OP_LT_TRUE\n" +
                " MOVE 0, R6\n" +
                " JR OP_LT_RET\n" +
                "OP_LT_TRUE  MOVE 1, R6\n" +
                "OP_LT_RET RET\n");
        //manji ili jednak od
        sb.append("OP_LTE LOAD R1, (R7 + 8)\n" +
                " LOAD R2, (R7 + 4)\n" +
                " CMP R1, R2\n" +
                " JR_SLE OP_LTE_TRUE\n" +
                " MOVE 0, R6\n" +
                " JR OP_LTE_RET\n" +
                "OP_LTE_TRUE  MOVE 1, R6\n" +
                "OP_LTE_RET RET\n");
        //mnozenje
        sb.append("OP_MUL LOAD R0, (R7 + 8)\n" +
                " LOAD R1, (R7 + 4)\n" +
                " MOVE 0, R2\n" +
                " CMP R1, 0 ; provjera predznaka operanda R1\n" +
                " JP_Z OP_MUL_KRAJ\n" +
                " JP_P OP_MUL_POZ\n" +
                "OP_MUL_NEG SUB R2, R0, R2\n" +
                " ADD R1, 1, R1\n" +
                " JP_NZ OP_MUL_NEG\n" +
                " JP OP_MUL_KRAJ\n" +
                "OP_MUL_POZ ADD R2, R0, R2\n" +
                " SUB R1, 1, R1\n" +
                " JP_NZ OP_MUL_POZ\n" +
                "OP_MUL_KRAJ MOVE R2, R6\n" +
                " RET\n");
        //dijeljenje
        sb.append("OP_DIV LOAD R0, (R7 + 8)\n" +
                " LOAD R1, (R7 + 4)\n" +
                " MOVE 0, R2\n" +
                " XOR R0, R1, R3\n" +
                "OP_DIV_TEST_1 OR R0, R0, R0\n" +
                " JR_Z OP_DIV_GOTOVO\n" +
                " JR_P OP_DIV_TEST_2\n" +
                "OP_DIV_NEGAT_1 XOR R0, -1, R0\n" +
                " ADD R0, 1, R0\n" +
                "OP_DIV_TEST_2 OR R1, R1, R1\n" +
                " JR_Z OP_DIV_GOTOVO\n" +
                " JR_P OP_DIV_PETLJA\n" +
                "OP_DIV_NEGAT_2 XOR R1, -1, R1\n" +
                " ADD R1, 1, R1\n" +
                "OP_DIV_PETLJA SUB R0, R1, R0\n" +
                " JR_ULT OP_DIV_PREDZNAK\n" +
                " ADD R2, 1, R2\n" +
                " JR OP_DIV_PETLJA\n" +
                "OP_DIV_PREDZNAK ROTL R3, 1, R3\n" +
                " JR_NC OP_DIV_GOTOVO\n" +
                "OP_DIV_RAZLICIT XOR R2, -1, R2\n" +
                " ADD R2, 1, R2\n" +
                "OP_DIV_GOTOVO MOVE R2, R6\n" +
                " RET\n");
        //ostatak pri cjelobrojnom dijeljenju
        sb.append("OP_MOD LOAD R0, (R7 + 8)\n" +
                " LOAD R1, (R7 + 4)\n" +
                " OR R0, R0, R0\n" +
                " JR_Z OP_MOD_GOTOVO\n" +
                "OP_MOD_PETLJA CMP R0, R1\n" +
                " JR_ULT OP_MOD_GOTOVO\n" +
                " SUB R0, R1, R0\n" +
                " JR OP_MOD_PETLJA\n" +
                "OP_MOD_GOTOVO MOVE R0, R6\n" +
                " RET\n");
        //logcko negiranje
        sb.append("OP_NEG LOAD R0, (R7 + 4)\n" +
                " OR R0, R0, R0\n" +
                " JR_Z OP_NEG_TRUE\n" +
                " MOVE 0, R6\n" +
                " RET\n" +
                "OP_NEG_TRUE\n" +
                " MOVE 1, R6\n" +
                " RET\n");
        //kopiranje niza
        sb.append("COPY_ARRAY LOAD R0, (R7 + 0C)\n" +
                " LOAD R1, (R7 + 8)\n" +
                " LOAD R2, (R7 + 4)\n" +
                "COPY_ARRAY_PETLJA LOAD R3, (R0)\n" +
                " STORE R3, (R1)\n" +
                " ADD R0, 4, R0\n" +
                " ADD R1, 4, R1\n" +
                " SUB R2, 1, R2\n" +
                " JR_NZ COPY_ARRAY_PETLJA\n" +
                " RET\n");

        return sb.toString();
    }
}
