package Process;

import java.util.*;


public class PCBList {

    /** Instance of PCBList */
    public static final PCBList list = new PCBList();
    private List<PCB> data;
    private List<Integer> usedPids;
    private Random generator;
    //public Processor processor = new Processor(this);

    public PCBList() {
        data = new ArrayList<>();
        usedPids = new ArrayList<>();
        generator = new Random();
    }



    //Generates an unique process id (pid) and adds it to used adresses arraylist
    private int pidGen(){
        boolean notIn = false;
        int temp = -1;
        while (!notIn){
            notIn = true;
            temp = generator.nextInt(850) + 101;
            for (int e: usedPids){
                if (e == temp) {
                    notIn = false;
                    break;
                }
            }
        }
        usedPids.add(temp);
        return temp;
    }

    public static final int DUMMY_ID = 0;

    public void addDummy(final String dummyExec) {
        //To trzeba bedzie innaczej zrobic
    }


    public void newProcess(final String name, final int priority, String exec){
        //To trzeba bedzie innaczej zrobic
    }

    //raczej niepotrzebne
    public Vector<Byte> toObjects(byte[] bytesPrim) {
        Vector<Byte> bytes = new Vector<>();

        int i = 0;
        for (byte b : bytesPrim)  bytes.add(i++, b); // Autoboxing

        return bytes;
    }

    //raczej niepotrzebne
    public byte[] toPrimitives(Vector<Byte> oBytes)
    {
        byte[] bytes = new byte[oBytes.size()];

        for(int i = 0; i < oBytes.size(); i++) {
            bytes[i] = oBytes.elementAt(i);
        }

        return bytes;
    }

    public void makeProcessWait(final PCB process) {
        //To trzeba bedzie innaczej zrobic
    }

    //Nie mam pojecia co to ma robic na razie ~MB
    //public void signal(final PCB process) {
    //    process.setState(ProcessState.READY);
    //    processor.addReadyProcess(process, true);
    //}


    public void deleteProcess(int pid){
        Iterator itr = data.iterator();
        while (itr.hasNext()){
            PCB temp = (PCB) itr.next();
            if (temp.getPID() == pid){
                //Nie mamy ani logow ani pamieci wirtualnej
                //Utils.log("Deleted process \"" + temp.getName() +
                //        "\", PID: " + temp.getPID());
                //vm.removeProcess(temp.getPID());

                itr.remove();
            }
        }
    }

    //for testing
    public PCB findByName(String name){
        for (PCB e: data){
            if (e.getName().equals(name)) return e;
        }
        return null;
    }

    public PCB findByPID(int pid){
        for (PCB e: data){
            if (e.getPID() == pid) return e;
        }
        return null;
    }

    public List<PCB> getData() {
        return data;
    }

    //??? ~MB
    //public void print(){
    //    for (final PCB pcb: data) Shell.println(pcb.toString());
    //}

}
