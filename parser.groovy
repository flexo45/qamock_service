def payments_data = "C:\\Work\\test123\\TRISTERO_CHARGES_MV.csv";
def tp_data = "C:\\Work\\test123\\TP_NOTIFICATIONS_TRISTER.csv";

BufferedReader bufferedReader = new BufferedReader(new FileReader(payments_data))

String line

List<Map<String, String>> payment = []
List<Map<String, String>> tp = []
List<Map> mismatch = []
Map<String, String> match = null

while ((line = bufferedReader.readLine()) != null){
    def lead_id = line.split(";")[0].replace("\"", "")
    def count = line.split(";")[1].replace("\"", "")
    payment.add([lead_id:lead_id, count:count])
}


bufferedReader = new BufferedReader(new FileReader(tp_data))

while ((line = bufferedReader.readLine()) != null){
    def lead_id = line.split(";")[0].replace("\"", "")
    def count = line.split(";")[1].replace("\"", "")
    tp.add([lead_id:lead_id, count:count])
}

tp.each {
    i_tp ->
        match = null

        System.out.println(tp.indexOf(i_tp))

        payment.each {
            py ->
                def p_l = py.get("lead_id")
                def tp_l = i_tp.get("lead_id")
                if(p_l.equals(tp_l)){
                    match = py
                }
        }

        if(match == null){
            mismatch.add(i_tp)
            System.out.println(i_tp)
        }
}