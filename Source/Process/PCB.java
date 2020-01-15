package Process;

import java.util.Vector;
import java.util.Stack;

public class PCB {

// ================================================= v Licznik rozkazów (Instruction Pointer) i zawartości rejestrów
    private Integer m_IP, m_AX, m_BX, m_CX, m_DX;
    private String m_name;
    private Byte m_PID;
    private String m_state;
// ================================================= v Tau
    private Double m_expectedTime;
    private Stack<Byte> m_pageTable;
    private String m_filePath;
    private Vector</*Plik*/> openFiles;

// ================================================= v Metoda zwracająca stan rejestrów i licznika rozkazów jako String
    public String regToString() {
        return "AX: " + m_AX.toString() + "\t" + "BX: " + m_BX.toString() + "\t" +
                "CX: " + m_CX.toString() + "\t" + "DX: " + m_DX.toString() + "\t" +
                "IP: " + m_IP.toString();
    }

// ================================================= v Konstruktor
    public PCB(String name, Byte PID, String state, Double expectedTime, String filePath) {

        m_name = name;
        m_PID = PID;
        m_state = state;
        m_expectedTime = expectedTime;
        m_filePath = filePath;

        m_IP = m_AX = m_BX = m_CX = m_DX = 0;
        m_pageTable = new Stack<>();
        m_openFiles = new Vector<>();
    }

// ================================================= v Gettery i settery
    public String getName() {return m_name;}

    public Byte getPID() {return m_PID;}

    public String getState() {return m_state;}
    public void setState(String state) {m_state = state;}

    public Double getExpectedTime() {return m_expectedTime;}
    public void setExpectedTime(Double expectedTime) {m_expectedTime = expectedTime;}

    public Integer getIP() {return m_IP;}
    public void setIP(Integer IP) {m_IP = IP;}

    public Integer getAX() {return m_AX;}
    public void setAX(Integer AX) {m_AX = AX;}

    public Integer getBX() {return m_BX;}
    public void setBX(Integer BX) {m_BX = BX;}

    public Integer getCX() {return m_CX;}
    public void setCX(Integer CX) {m_CX = CX;}

    public Integer getDX() {return m_DX;}
    public void setDX(Integer DX) {m_DX = DX;}
}